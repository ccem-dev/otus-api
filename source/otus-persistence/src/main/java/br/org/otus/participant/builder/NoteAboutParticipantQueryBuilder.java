package br.org.otus.participant.builder;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.ccem.otus.model.searchSettingsDto.SearchSettingsDto;
import org.ccem.otus.service.ParseQuery;

import java.util.ArrayList;

public class NoteAboutParticipantQueryBuilder {

  private ArrayList<Bson> pipeline;

  public ArrayList<Bson> getByRnQuery(ObjectId userOid, Long recruitmentNumber, SearchSettingsDto searchSettingsDto) {
    int skip = searchSettingsDto.getCurrentQuantity();
    int limit = searchSettingsDto.getQuantityToGet();

    pipeline = new ArrayList<>();
    pipeline.add(ParseQuery.toDocument("{\n" +
      "        $match: {\n" +
      "            \"recruitmentNumber\": " + recruitmentNumber +
      "        }\n" +
      "    }"));
    pipeline.add(ParseQuery.toDocument("{\n" +
      "        $lookup: {\n" +
      "            from: \"user\",\n" +
      "            let: {\n" +
      "                \"userId\": \"$userId\"\n" +
      "            },\n" +
      "            pipeline: [\n" +
      "                {\n" +
      "                    $match: {\n" +
      "                        $expr: {\n" +
      "                            $eq: [\"$_id\", \"$$userId\"]\n" +
      "                        }\n" +
      "                    }\n" +
      "                },\n" +
      "                {\n" +
      "                    $project: {\n" +
      "                        \"name\": {\n" +
      "                            $concat: [\"$name\", \" \", \"$surname\" ]\n" +
      "                        }\n" +
      "                    }\n" +
      "                }\n" +
      "            ],\n" +
      "            as: \"user\"\n" +
      "        }\n" +
      "    }"));
    pipeline.add(ParseQuery.toDocument("{\n" +
      "        $addFields: {\n" +
      "            \"userName\": {\n" +
      "                $ifNull: [ { $arrayElemAt: [\"$user.name\",0] }, null ]\n" +
      "            },\n" +
      "            \"isCreator\": {\n" +
      "                $eq: [ {$toString: \"$userId\"}, " + userOid.toHexString()+ "]\n" +
      "            }\n" +
      "        }\n" +
      "    }"));
    pipeline.add(ParseQuery.toDocument("{\n" +
      "        $project: {\n" +
      "            \"userId\": 0,\n" +
      "            \"user\": 0\n" +
      "        }\n" +
      "    }"));
    pipeline.add(ParseQuery.toDocument("{\n" +
      "      $sort: {\n" +
      "            \"creationDate\": -1\n" +
      "        }\n" +
      "    }"));
    pipeline.add(ParseQuery.toDocument("{ $skip: " + skip + " }"));
    pipeline.add(ParseQuery.toDocument("{ $limit: " + limit + " }"));
    return pipeline;
  }

}
