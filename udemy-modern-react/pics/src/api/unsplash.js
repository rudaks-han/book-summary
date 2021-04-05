import axios from 'axios';

export default axios.create({
	baseURL: 'https://api.unsplash.com',
	headers: {
		Authorization: 'Client-ID 3699261deb2ed988e77d25acc355b0b0a46753a0bb2571a0af447985323c752a'
	}
});