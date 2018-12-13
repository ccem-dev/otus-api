package br.org.otus.monitoring.builder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LaboratoryProgressQueryBuilderTest {
    private static String ORPHANS_QUERY = "[{\"$match\":{\"aliquotValid\":false}},{\"$group\":{\"_id\":{\"examId\":\"$examId\",\"examName\":\"$examName\"}}},{\"$group\":{\"_id\":\"$_id.examName\",\"examIds\":{\"$push\":\"$_id.examId\"},\"count\":{\"$sum\":1.0}}},{\"$group\":{\"_id\":{},\"orphanExamsProgress\":{\"$push\":{\"title\":\"$_id\",\"orphans\":\"$count\"}}}}]";
    private static String EXAM_QUERY = "[{\"$match\":{\"role\":\"EXAM\",\"fieldCenter.acronym\":\"RS\"}},{\"$group\":{\"_id\":{},\"aliquotCodes\":{\"$push\":\"$code\"}}},{\"$lookup\":{\"from\":\"exam_result\",\"let\":{\"aliquotCodes\":\"$aliquotCodes\"},\"pipeline\":[{\"$match\":{\"$expr\":{\"$and\":[{\"$in\":[\"$aliquotCode\",\"$$aliquotCodes\"]}]}}},{\"$group\":{\"_id\":{\"examId\":\"$examId\",\"examName\":\"$examName\"}}},{\"$group\":{\"_id\":\"$_id.examName\",\"received\":{\"$sum\":1.0}}},{\"$group\":{\"_id\":{},\"examsQuantitative\":{\"$push\":{\"title\":\"$_id\",\"exams\":\"$received\"}}}}],\"as\":\"receivedCount\"}},{\"$project\":{\"examsQuantitative\":{\"$arrayElemAt\":[\"$receivedCount.examsQuantitative\",0.0]}}}]";
    private static String PENDING_RESULT_QUERY = "[{\"$match\":{\"fieldCenter.acronym\":\"RS\"}},{\"$group\":{\"_id\":\"$name\",\"aliquotsInDb\":{\"$push\":\"$code\"}}},{\"$lookup\":{\"from\":\"exam_result\",\"let\":{\"aliquotCodes\":\"$aliquotsInDb\"},\"pipeline\":[{\"$match\":{\"$expr\":{\"$and\":[{\"$in\":[\"$aliquotCode\",\"$$aliquotCodes\"]},{\"$eq\":[\"$aliquotValid\",true]}]}}},{\"$group\":{\"_id\":{\"examId\":\"$examId\",\"examName\":\"$examName\",\"aliquotCode\":\"$aliquotCode\"}}},{\"$group\":{\"_id\":\"$_id.aliquotCode\",\"aliquots\":{\"$push\":\"$_id.aliquotCode\"}}},{\"$group\":{\"_id\":{},\"aliquots\":{\"$push\":\"$_id\"}}}],\"as\":\"aliquotswithResults\"}},{\"$group\":{\"_id\":{},\"pendingResultsByAliquot\":{\"$push\":{\"title\":\"$_id\",\"waiting\":{\"$cond\":{\"if\":{\"$gte\":[{\"$size\":\"$aliquotswithResults\"},1.0]},\"then\":{\"$subtract\":[{\"$size\":\"$aliquotsInDb\"},{\"$size\":{\"$arrayElemAt\":[\"$aliquotswithResults.aliquots\",0.0]}}]},\"else\":{\"$size\":\"$aliquotsInDb\"}}},\"received\":{\"$cond\":{\"if\":{\"$gte\":[{\"$size\":\"$aliquotswithResults\"},1.0]},\"then\":{\"$size\":{\"$arrayElemAt\":[\"$aliquotswithResults.aliquots\",0.0]}},\"else\":0.0}}}}}}]";
    private static String QUANTITATIVE_QUERY = "[{\"$match\":{\"role\":\"EXAM\",\"fieldCenter.acronym\":\"RS\"}},{\"$group\":{\"_id\":\"$name\",\"aliquots\":{\"$push\":{\"code\":\"$code\",\"transported\":{\"$cond\":{\"if\":{\"$ne\":[\"$transportationLotId\",null]},\"then\":1.0,\"else\":0.0}},\"prepared\":{\"$cond\":{\"if\":{\"$ne\":[\"$examLotId\",null]},\"then\":1.0,\"else\":0.0}}}}}},{\"$unwind\":\"$aliquots\"},{\"$group\":{\"_id\":\"$_id\",\"transported\":{\"$sum\":\"$aliquots.transported\"},\"prepared\":{\"$sum\":\"$aliquots.prepared\"},\"aliquots\":{\"$push\":\"$aliquots.code\"}}},{\"$lookup\":{\"from\":\"exam_result\",\"let\":{\"aliquotCode\":\"$aliquots\"},\"pipeline\":[{\"$match\":{\"$expr\":{\"$and\":[{\"$in\":[\"$aliquotCode\",\"$$aliquotCode\"]}]}}},{\"$group\":{\"_id\":\"$examId\",\"received\":{\"$push\":\"$aliquotCode\"}}},{\"$count\":\"count\"}],\"as\":\"receivedCount\"}},{\"$group\":{\"_id\":{},\"quantitativeByTypeOfAliquots\":{\"$push\":{\"title\":\"$_id\",\"transported\":\"$transported\",\"prepared\":\"$prepared\",\"received\":{\"$cond\":{\"if\":{\"$gte\":[{\"$size\":\"$receivedCount\"},1.0]},\"then\":{\"$arrayElemAt\":[\"$receivedCount.count\",0.0]},\"else\":0.0}}}}}}]";
    private static String STORAGE_QUERY = "[{\"$match\":{\"role\":\"STORAGE\",\"fieldCenter.acronym\":\"RS\"}},{\"$group\":{\"_id\":\"$name\",\"count\":{\"$sum\":1.0}}},{\"$group\":{\"_id\":{},\"storageByAliquot\":{\"$push\":{\"title\":\"$_id\",\"storage\":\"$count\"}}}}]";
    private static String PENDING_RESULT_CSV_QUERY = "[{\"$match\":{\"role\":\"EXAM\",\"fieldCenter.acronym\":\"RS\"}},{\"$project\":{\"code\":\"$code\",\"transported\":{\"$cond\":{\"if\":{\"$ne\":[\"$transportationLotId\",null]},\"then\":1.0,\"else\":0.0}},\"prepared\":{\"$cond\":{\"if\":{\"$ne\":[\"$examLotId\",null]},\"then\":1.0,\"else\":0.0}}}},{\"$lookup\":{\"from\":\"exam_result\",\"localField\":\"code\",\"foreignField\":\"aliquotCode\",\"as\":\"results\"}},{\"$match\":{\"results\":{\"$exists\":true,\"$size\":0.0}}},{\"$group\":{\"_id\":{},\"pendingAliquotsCsvData\":{\"$push\":{\"aliquot\":\"$code\",\"transported\":\"$transported\",\"prepared\":\"$prepared\"}}}}]";
    private static String ORPHAN_CSV_QUERY = "[{\"$match\":{\"aliquotValid\":false}},{\"$group\":{\"_id\":{\"examId\":\"$examId\",\"aliquotCode\":\"$aliquotCode\",\"examName\":\"$examName\"}}},{\"$group\":{\"_id\":{},\"orphanExamsCsvData\":{\"$push\":{\"aliquotCode\":\"$_id.aliquotCode\",\"examName\":\"$_id.examName\"}}}}]";
    private static String CENTER = "RS";
    private static Gson builder;

    @Before
    public void setUp() {
        builder = new GsonBuilder().create();
    }

    @Test
    public void getOrphansQuery() {
        assertEquals(ORPHANS_QUERY, builder.toJson(new LaboratoryProgressQueryBuilder().getOrphansQuery()));
    }

    @Test
    public void getDataByExamQuery() {
        assertEquals(EXAM_QUERY, builder.toJson(new LaboratoryProgressQueryBuilder().getDataByExamQuery(CENTER)));
    }

    @Test
    public void getPendingResultsQuery() {
        assertEquals(PENDING_RESULT_QUERY, builder.toJson(new LaboratoryProgressQueryBuilder().getPendingResultsQuery(CENTER)));
    }

    @Test
    public void getQuantitativeQuery() {
        assertEquals(QUANTITATIVE_QUERY, builder.toJson(new LaboratoryProgressQueryBuilder().getQuantitativeQuery(CENTER)));
    }

    @Test
    public void getStorageByAliquotQuery() {
        assertEquals(STORAGE_QUERY, builder.toJson(new LaboratoryProgressQueryBuilder().getStorageByAliquotQuery(CENTER)));
    }

    @Test
    public void getCSVOfPendingResultsQuery() {
        assertEquals(PENDING_RESULT_CSV_QUERY, builder.toJson(new LaboratoryProgressQueryBuilder().getCSVOfPendingResultsQuery(CENTER)));
    }

    @Test
    public void getCSVOfOrphansByExamQuery() {
        assertEquals(ORPHAN_CSV_QUERY, builder.toJson(new LaboratoryProgressQueryBuilder().getCSVOfOrphansByExamQuery()));
    }
}
