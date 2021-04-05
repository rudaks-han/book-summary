import React from 'react';

class SearchBar extends React.Component {
	state = { term: '' };

	onFormSubmit = e => {
		e.preventDefault();

		this.props.onFormSubmit(this.state.term);
	}

	onChangeInput = e => {
		this.setState({
			term : e.target.value
		});
	};

	render() {
		return (
			<div className="search-bar ui segment">
				<form
					onSubmit={this.onFormSubmit}
				>
				<input
					type="text"
					onChange={this.onChangeInput}
				/>
				</form>
			</div>
		)
	}
};

export default SearchBar;