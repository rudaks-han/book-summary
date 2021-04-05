export default (state = [], action) => {
	switch (action.type) {
		case 'FETCH_POSTS':
			//return [...state, action.payload];
			return action.payload;
		default:
			return state;
	}
}