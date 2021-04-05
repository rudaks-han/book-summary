import React from 'react';

class SearchBar extends React.Component {
	state = {
		term: ''
	};

	onFormSubmit = e => {
		e.preventDefault();

		this.props.onSubmit(this.state.term);
	}

	render() {
		return (
			<div onSubmit={this.onFormSubmit} className="ui segment">
				<form className="ui form">
					<div className="field">
						<label>Image Search</label>
						<input type="text" value={this.state.term} onChange={(e) => this.setState({term: e.target.value})}/>
					</div>
				</form>
			</div>
		);
	}
};

export default SearchBar;