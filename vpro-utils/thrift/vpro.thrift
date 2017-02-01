namespace java rise.core.utils.tecs

typedef i64 Time

# Messages related to the JSON structure to communicate with VU Amsterdam #
##################################

const string RobotPresenterDataMsg = "RobotPresenterData"

# this message contains the data 
struct RobotPresenterData{
	1:required i32 structure_prepositional_phrases_count;
	2:required i32 structure_adjective_count;
	3:required i32 structure_non_future;
	4:required i32 structure_active_sentences;
	5:required i32 structure_personal_pronouns;
	6:required i32 structure_word_length;
	7:required i32 structure_negations;
	8:required i32 structure_adverbs;
	9:required i32 structure_passive_sentences;
	10:required i32 structure_number_of_sentences;
	11:required i32 structure_future;
	12:required i32 structure_wordcount;
	13:required string metadata_speaker;
	14:required string metadata_lang;
	15:required string metadata_timestamp;
	16:required string metadata_input_text;
	17:required string metadata_confidence_score;
	18:required list<string> metadata_other_possible_responses;
	19:required list<string> metadata_people_present;
	20:required string metadata_script_id; 
	21:required list<double> metadata_location_geometry_coordinates;
	22:required string metadata_location_geometry_type;
	23:required string metadata_location_type;
	24:required list<string> metadata_location_properties_name;
	25:required string metadata_scene_id; 
	26:required list<string> semantic_keywords;
	27:required list<string> semantic_organisations;
	28:required list<string> semantic_people;
	29:required list<string> semantic_places;
	30:required list<string> semantic_topics;
	31:required list<string> emotions_detected_emotion;
	32:required list<string> emotions_information_state;
}


const string ProcessedRobotPresenterDataMsg = "ProcessedRobotPresenterData"

# this message contains the data 
struct ProcessedRobotPresenterData{
	1:required i32 structure_prepositional_phrases_count;
	2:required i32 structure_adjective_count;
	3:required i32 structure_non_future;
	4:required i32 structure_active_sentences;
	5:required i32 structure_personal_pronouns;
	6:required i32 structure_word_length;
	7:required i32 structure_negations;
	8:required i32 structure_adverbs;
	9:required i32 structure_passive_sentences;
	10:required i32 structure_number_of_sentences;
	11:required i32 structure_future;
	12:required i32 structure_wordcount;
	13:required string metadata_speaker;
	14:required string metadata_lang;
	15:required string metadata_timestamp;
	16:required string metadata_input_text;
	17:required string metadata_confidence_score;
	18:required list<string> metadata_other_possible_responses;
	19:required list<string> metadata_people_present;
	20:required string metadata_script_id; 
	21:required list<double> metadata_location_geometry_coordinates;
	22:required string metadata_location_geometry_type;
	23:required string metadata_location_type;
	24:required list<string> metadata_location_properties_name;
	25:required string metadata_scene_id; 
	26:required list<string> semantic_keywords;
	27:required list<string> semantic_organisations;
	28:required list<string> semantic_people;
	29:required list<string> semantic_places;
	30:required list<string> semantic_topics;
	31:required list<string> emotions_detected_emotion;
	32:required list<string> emotions_information_state;
}


######################################

const string GetSpeechMsg = "GetSpeech"

# this message contains the information needed to obtain Speech Recognition output(s)
struct GetSpeech{
	1:required i32 id;
	2:required i32 duration;
	3:required string language;
	4:required i32 alternatives;	
}

const string BehaviourMsg = "Behaviour"

# this message contains the information needed to generate the behavior of the (virtual) NAO
struct Behaviour {
	1:required i32 id;
	2:required string gesture;
	3:required string textToSpeak;
	4:optional string type;
	5:optional double mood;
	6:optional i32 speechSpeed;
	7:optional double speechPitchShift;
	8:optional i32 speechShape;
	
}

const string LowLevelNaoCommandMsg = "LowLevelNaoCommand"

# this message contains commands send to the NAO
# [startbehavior, stopbehavior, getposture, gotoposture]
struct LowLevelNaoCommand {
	1:required i32 id;
	2:required string command;
}




