import { combineReducers} from "redux/es/redux";

const songReducer = () => {
	return [
		{ title: 'song title1', duration: '1:00'},
		{ title: 'song title2', duration: '2:00'},
		{ title: 'song title3', duration: '3:00'},
		{ title: 'song title4', duration: '4:00'},
	];
};

const selectedSongReducer = (selectedSong = null, action) => {
	if (action.type === 'SONG_SELECTED') {
		return action.payload;
	}

	return selectedSong;
};

export default combineReducers({
	songs: songReducer,
	selectedSong: selectedSongReducer
});