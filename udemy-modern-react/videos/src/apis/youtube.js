import axios from 'axios';

const KEY = 'AIzaSyB1egJgw39goLZIr3wP0ot-cbEmmnmqpi8';

export default axios.create({
	baseURL: 'https://www.googleapis.com/youtube/v3',
	params: {
		part: 'snippet',
		maxResults: 5,
		key: KEY
	}
});


