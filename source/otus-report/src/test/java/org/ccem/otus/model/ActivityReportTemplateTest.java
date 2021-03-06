package org.ccem.otus.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
public class ActivityReportTemplateTest {
  private static String EXPECTED_ACTIVITY_REPORT_TEMPLATE = "{\"acronym\":\"RETCLQ\",\"versions\":[3],\"template\":\"  \\n<otus-script>\\n</otus-script>\\n<div>\\n  <style type=\\\"text/css\\\">\\n    img {\\n      display: block;\\n      margin-left: auto;\\n      margin-right: auto;\\n      margin-bottom: 0.5cm;\\n      width: 80mm;\\n    }\\n\\n    hr {\\n      border-top: 1.5pt solid #000000;\\n    }\\n\\n    .footer{\\n      width: 100%;\\n      border-top: 2.0pt solid #000000;\\n      font-family: \\\"Arial\\\", \\\"serif\\\";\\n      font-size: 12px;\\n      text-align: center;\\n      position: absolute; \\n    }\\n\\n    .footer-1{ \\n      top: 260mm; \\n    }\\n\\n    .footer-2{ \\n      top: 537.5mm; \\n    }\\n    \\n    .footer-3{ \\n      top: 814mm; \\n    }\\n\\n    .participantInfo {\\n      display: flex;\\n      border-bottom: 2.0pt solid #000000;\\n    }\\n\\n    .column {\\n      flex: 20%;\\n      font-family: \\\"Verdana\\\", \\\"serif\\\";\\n      font-size: 12px;\\n      font-weight: bold;\\n    }\\n\\n    .contextValues {\\n      font-family: \\\"Verdana\\\", \\\"serif\\\";\\n      font-size: 12px;\\n      font-weight: bold;\\n    }\\n\\n    .contextValues p:first-of-type {\\n      font-size: 14px;\\n    }\\n\\n    .contextObs {\\n      font-family: \\\"Verdana\\\", \\\"serif\\\";\\n      font-size: 12px;\\n    }\\n\\n    .break {\\n      page-break-before: always;\\n    }\\n    p{\\n      margin: 0.5em 0;\\n    }\\n  </style>\\n  <header>\\n    <!-- TODO: Substituir imagem -->\\n    <img src=\\\"http://wiki.inf.otus-solutions.com.br/images/d/d4/ELSA-logo.jpg\\\">\\n  </header>\\n\\n  <section class=\\\"participantInfo\\\">\\n    <section class=\\\"column\\\">\\n      Nome: {{data.participant.name}}\\n      <br> Sexo: {{data.sexo}}\\n      <br> Data de Nascimento: {{data.nascimento}}\\n      <br>\\n    </section>\\n    <section class=\\\"column\\\">\\n      Número de Recrutamento: {{data.participant.recruitmentNumber}}\\n      <br> Data da coleta: {{data.date}}\\n    </section>\\n  </section>\\n\\n  <section class=\\\"contextValues\\\">\\n    <p>Relatório de Atividade</p>\\n    <br/>\\n    <p>Formulário: CENTRO DE LEITURA DE RETINOGRAFIA</p>\\n  </section>\\n\\n  <section class=\\\"contextObs\\\">\\n    <span ng-if=\\\"data.exameColesterolTotalFracoesObs\\\">\\n      <p>Obs: {{data.exameColesterolTotalFracoesObs}}</p>\\n      <br>\\n    </span>\\n    <p>Sigla : RETCLQ</p>\\n    <p>Questão 1 : true</p>\\n    </section>\\n\\n   <footer class=\\\"footer footer-1\\\">\\n    <p>Responsável técnico: Ligia Maria Giongo Fedeli - CRF SP 10491 - Pagina 1</p>\\n  </footer>\\n\\n\",\"label\":\"Titulo do relatório\",\"sender\":\"fdrtec@gmail.com\",\"sendingDate\":\"2018-11-07T15:51:53.725Z\",\"dataSources\":[{\"key\":\"self\",\"dataSource\":\"ActivityReportAnswerFilling\",\"label\":\"Questões de RETCLQ\",\"result\":[],\"optional\":true}]}";
  private static String ACRONYM = "ACTA";
  private static Integer VERSION = 1;

  private ActivityReportTemplate activityReportTemplate;

  @Test
  public void deserialize_method_should_return_expected_ActivityReportTemplate_with_elements() {
    ReportTemplate reportTemplate = ActivityReportTemplate.deserialize(EXPECTED_ACTIVITY_REPORT_TEMPLATE);
    assertEquals(EXPECTED_ACTIVITY_REPORT_TEMPLATE, ActivityReportTemplate.serialize(reportTemplate));
  }

  @Test
  public void getAcronymMethod_should_return_acronym() {
    activityReportTemplate = new ActivityReportTemplate();
    Whitebox.setInternalState(activityReportTemplate, "acronym", ACRONYM);

    assertEquals(ACRONYM, activityReportTemplate.getAcronym());
  }

  @Test
  public void getVersionMethod_should_return_version() {
    activityReportTemplate = new ActivityReportTemplate();
    List<Integer> versions = new ArrayList();
    versions.add(VERSION);

    Whitebox.setInternalState(activityReportTemplate, "versions", versions);

    assertEquals(versions, activityReportTemplate.getVersions());
  }
}