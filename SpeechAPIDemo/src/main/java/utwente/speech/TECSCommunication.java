
//package utwente.speech;
//package SpeechAPIDemo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//import vpro.utils.tecs.*;
import rise.core.utils.tecs.*;

public class TECSCommunication {

	/* RobotPresenterData */
	static int structure_prepositional_phrases_count = 0;
	static int structure_adjective_count = 0;
	static int structure_non_future = 0;
	static int structure_active_sentences = 0;
	static int structure_personal_pronouns = 0;
	static int structure_word_length = 0;
	static int structure_negations = 0;
	static int structure_adverbs = 0;
	static int structure_passive_sentences = 0;
	static int structure_number_of_sentences = 0;
	static int structure_future = 0;
	static int structure_wordcount = 0;
	static String metadata_speaker = "";
	static String metadata_lang = "";
	static String metadata_timestamp = "";
	static String metadata_input_text = "";
	static String metadata_confidence_score = "";
	static List<String> metadata_other_possible_responses = new ArrayList<String>();
	static List<String> metadata_people_present = new ArrayList<String>();
	static String metadata_script_id = "";
	static List<Double> metadata_location_geometry_coordinates = new ArrayList<Double>();
	static String metadata_location_geometry_type = "";
	static String metadata_location_type = "";
	static List<String> metadata_location_properties_name = new ArrayList<String>();
	static String metadata_scene_id = "";
	static List<String> semantic_keywords = new ArrayList<String>();
	static List<String> semantic_organisations = new ArrayList<String>();
	static List<String> semantic_people = new ArrayList<String>();
	static List<String> semantic_places = new ArrayList<String>();
	static List<String> semantic_topics = new ArrayList<String>();
	static List<String> emotions_detected_emotion = new ArrayList<String>();
	static List<String> emotions_information_state = new ArrayList<String>();

	public static TECSClient clientTECS = null;

	// public static void connectToTECS(final String clientName) {
	public static void connectToTECS(final String hostIP, final String clientName, final int port) {
		Thread inputListener = new Thread() {
			@Override
			public void run() {
				System.out.println("Connecting to TECS Server - " + hostIP + ":" + port);
				clientTECS = new TECSClient(hostIP, clientName, port);

				if (clientTECS != null) {
					// clientTECS.subscribe(riseConstants.ProcessedRobotPresenterDataMsg,
					// ProcessedRobotPresenterDataEventHandler);

					clientTECS.startListening();
					System.out.println("Connected to TECS Server");

				}
			}
		};
		inputListener.start();
	}

	public void disconnectToTECS() {
		clientTECS.disconnect();
		if (clientTECS != null) {
			clientTECS = null;
		}

		System.out.println("Disconnected to TECS Server!");

	}

	/* Sending Recognized Speech to server */

	public static void sendRecognizedSpeech() {

		metadata_people_present.clear();
		metadata_location_geometry_coordinates.clear();
		metadata_location_properties_name.clear();

		/* Metadata */
		metadata_speaker = "Arjan";
		metadata_lang = SpeechAPIDemo.RecordingLanguage;

		Timestamp stamp = new Timestamp(System.currentTimeMillis());
		metadata_timestamp = stamp.toString();

		metadata_input_text = SpeechAPIDemo.Transcript;
		metadata_confidence_score = SpeechAPIDemo.ConfidenceScore;
		metadata_other_possible_responses.clear();

		metadata_people_present.add("Arjan");
		metadata_people_present.add("Pepper");

		metadata_script_id = "episode_1";

		metadata_location_geometry_coordinates.add(52.090587);
		metadata_location_geometry_coordinates.add(5.121719);

		metadata_location_geometry_type = "Point";
		metadata_location_type = "Feature";

		metadata_location_properties_name.add("Utrecht");
		metadata_location_properties_name.add("Netherlands");

		metadata_scene_id = "scene_1";

		clientTECS.send(new RobotPresenterData(structure_prepositional_phrases_count, structure_adjective_count,
				structure_non_future, structure_active_sentences, structure_personal_pronouns, structure_word_length,
				structure_negations, structure_adverbs, structure_passive_sentences, structure_number_of_sentences,
				structure_future, structure_wordcount, metadata_speaker, metadata_lang, metadata_timestamp,
				metadata_input_text, metadata_confidence_score, metadata_other_possible_responses,
				metadata_people_present, metadata_script_id, metadata_location_geometry_coordinates,
				metadata_location_geometry_type, metadata_location_type, metadata_location_properties_name,
				metadata_scene_id, semantic_keywords, semantic_organisations, semantic_people, semantic_places,
				semantic_topics, emotions_detected_emotion, emotions_information_state));

		System.out.println("Recognized Speech is transmitted");

	}

}
