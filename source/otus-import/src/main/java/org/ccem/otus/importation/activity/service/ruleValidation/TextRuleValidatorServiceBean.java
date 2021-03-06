package org.ccem.otus.importation.activity.service.ruleValidation;

import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.model.survey.activity.filling.AnswerFill;
import org.ccem.otus.model.survey.activity.filling.answer.TextAnswer;
import org.ccem.otus.survey.template.navigation.route.Rule;

public class TextRuleValidatorServiceBean implements TextRuleValidatorService {
  @Override
  public boolean run(Rule rule, AnswerFill answer) throws DataNotFoundException {
    String textRuleAnswer = rule.answer;
    TextAnswer textAnswer = (TextAnswer) answer;
    switch (rule.operator) {
      case "equal":
        if (!isEqual(textRuleAnswer, textAnswer.getValue())) {
          return false;
        }
        break;
      case "notEqual":
        if (isEqual(textRuleAnswer, textAnswer.getValue())) {
          return false;
        }
        break;
      case "contains":
        if (!contains(textRuleAnswer, textAnswer.getValue())) {
          return false;
        }
        break;
      default:
        throw new DataNotFoundException(new Throwable("Rule operator {" + rule.operator + "} for " + answer.getType() + " not found."));
    }
    return true;
  }

  private boolean contains(String textRuleAnswer, String answer) {
    return answer.contains(textRuleAnswer);
  }

  private boolean isEqual(String ruleAnswer, String answer) {
    return ruleAnswer.equals(answer);
  }
}
