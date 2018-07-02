package com.phantomartist.sinquizcity.game;

import java.util.ArrayList;

/**
 * Models a single question and potential answers as parsed from res/xml/questions.xml
 */
public class QuestionAndAnswers {

	public static final String QUESTION_TAG = "question";
	public static final String ASK_TAG = "ask";
	public static final String ANSWER_MAP_TAG = "answer-map";
	public static final String ANSWER_TAG = "answer";
	public static final String CORRECT_TAG = "correct";
	public static final String TRIVIA_TAG = "trivia";
	
	private int questionID;
	private String question;
	private ArrayList<Answer> answers;
	private int correctID;
	private String trivia;
	
	/**
	 * Lazy instantiation of ArrayList to save resources.
	 */
	public QuestionAndAnswers() {
		answers = new ArrayList<Answer>();
	}
	
	public int getQuestionID() {
		return questionID;
	}
	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public ArrayList<Answer> getAnswers() {
		return answers;
	}
	public void addAnswer(Integer answerID, String answer) {
		answers.add(new Answer(answerID, answer));
	}
	public int getCorrectID() {
		return correctID;
	}
	public void setCorrectID(int correctID) {
		this.correctID = correctID;
	}
	public String getTrivia() {
		return trivia;
	}
	public void setTrivia(String trivia) {
		this.trivia = trivia;
	}
	
	/**
	 * Returns the correct Answer object
	 * @return Answer the correct answer.
	 */
	public Answer getCorrectAnswer() {
		for (Answer answer : answers) {
			if (answer.getAnswerID() == getCorrectID()) {
				return answer;
			}
		}
		return null;
	}
	
	/**
	 * Returns true if the supplied answer is the correct one.
	 * @param answer The Answer
	 * @return boolean true if supplied answer is correct.
	 */
	public boolean isCorrectAnswer(Answer answer) {
		if (getCorrectID() == answer.getAnswerID()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Return true if question has trivia.
	 * 
	 * @boolean true if question has trivia attached.
	 */
	public boolean hasTrivia() {
		return getTrivia() != null;
	}
	
	@Override
	public String toString() {
		return "QuestionAndAnswers [questionID=" + questionID + ", question="
				+ question + ", answers=" + answersToString() + ", correctID="
				+ correctID + ", trivia=" + trivia + "]";
	}
	
	/**
	 * Return answers as a String.
	 * @return String answers as a String
	 */
	public String answersToString() {
		StringBuilder sb = new StringBuilder();
		if (answers.size() > 0) {
			for (int i = 0; i < answers.size(); i++) {
				sb.append("answerID=").append(i).append(", answer=").append(answers.get(i)).append(" ");
			}
		}
		return sb.toString();
	}
	
	/**
	 * Wraps answers.
	 */
	public class Answer {
		
		private int answerID;
		private String answer;
		
		public Answer(int id, String answer) {
			this.answerID = id;
			this.answer = answer;
		}
		public int getAnswerID() {
			return answerID;
		}
		public void setAnswerID(int answerID) {
			this.answerID = answerID;
		}
		public String getAnswer() {
			return answer;
		}
		public void setAnswer(String answer) {
			this.answer = answer;
		}
		
		public String toFullString() {
			return "Answer=[id=" + answerID + ", answer=" + answer + "]";
		}
		
		/**
		 * Used by ArrayAdapter to display value.
		 */
		@Override
		public String toString() {
			return answer;
		}
	}
}
