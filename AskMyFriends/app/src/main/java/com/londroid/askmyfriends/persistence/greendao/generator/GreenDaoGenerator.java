package com.londroid.askmyfriends.persistence.greendao.generator;
import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;


public class GreenDaoGenerator {

	public static void main(String args[]) throws IOException, Exception  {
			
		Schema schema = new Schema(1, "com.londroid.askmyfriends.persistence.greendao.domain");
		schema.setDefaultJavaPackageDao("com.londroid.askmyfriends.persistence.greendao.dao");
		generate(schema);
		new DaoGenerator().generateAll(schema, "src");
	
	}
	
	private static void generate(Schema schema) {
		
		Entity question = schema.addEntity("Question");
		Entity survey = schema.addEntity("Survey");
		Entity answer = schema.addEntity("Answer");
		Entity vote = schema.addEntity("Vote");
		Entity owner = schema.addEntity("Owner");
		Entity juror = schema.addEntity("Juror");
		Entity comment = schema.addEntity("Comment");
		// Simulate many to many
		Entity jurorSurvey = schema.addEntity("JurorSurvey");
		
		
		question.addIdProperty();
        question.addStringProperty("text");
        
        answer.addIdProperty();
        answer.addStringProperty("listingTag");
        answer.addStringProperty("text");
        answer.addIntProperty("order");
        Property surveyId = answer.addLongProperty("surveyId").notNull().getProperty();
        answer.addToOne(survey, surveyId);
        
        comment.addIdProperty();
        comment.addStringProperty("text");
        Property commentJurorId = comment.addLongProperty("jurorId").notNull().getProperty();
        Property commentVoteId = comment.addLongProperty("voteId").notNull().getProperty();
        Property commentSurveyId = comment.addLongProperty("surveyId").notNull().getProperty();
        comment.addToOne(vote, commentVoteId);
        comment.addToOne(juror, commentJurorId);
        
        vote.addIdProperty();
        Property answerId = vote.addLongProperty("answerId").notNull().getProperty();
        Property jurorId = vote.addLongProperty("jurorId").notNull().getProperty();
        Property commentId = vote.addLongProperty("commentId").getProperty();
        vote.addToOne(answer, answerId);
        vote.addToOne(juror, jurorId);
        vote.addToOne(comment, commentId);
        
        jurorSurvey.addIdProperty();
        Property jurorSurveyJurorId = jurorSurvey.addLongProperty("jurorId").notNull().getProperty();
        Property jurorSurveySurveyId = jurorSurvey.addLongProperty("surveyId").notNull().getProperty();
        jurorSurvey.addToOne(juror, jurorSurveyJurorId);
        jurorSurvey.addToOne(survey, jurorSurveySurveyId);
        
        juror.addIdProperty();
        juror.addStringProperty("name");
        juror.addStringProperty("phoneNumber").notNull();
        juror.addToMany(jurorSurvey, jurorSurveyJurorId, "surveys");
        juror.addToMany(comment, commentJurorId, "comments");
          
        survey.addIdProperty(); 
        survey.addStringProperty("title").notNull();  
        survey.addDateProperty("creationDate");
        survey.addDateProperty("modificationDate");
        survey.addStringProperty("choiceType"); // multiple or single choice
        Property questionId = survey.addLongProperty("questionId").notNull().getProperty();
        Property ownerId = survey.addLongProperty("ownerId").notNull().getProperty();
        survey.addToOne(question, questionId, "question");
        survey.addToMany(answer, surveyId, "answers");
        survey.addToMany(jurorSurvey, jurorSurveySurveyId, "jurors");
        survey.addToMany(comment, commentSurveyId, "comments");
        survey.addToOne(owner, ownerId, "owner");
        
        owner.addIdProperty();
        owner.addStringProperty("name");
        owner.addStringProperty("phoneNumber").notNull();
        owner.addToMany(survey, ownerId);
      
	}
}
