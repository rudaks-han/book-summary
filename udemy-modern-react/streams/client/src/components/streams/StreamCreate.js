import React from 'react';
import { Field, reduxForm } from 'redux-form';

class StreamCreate extends React.Component {
	renderError({ error, touched}) {
		if (touched && error) {}
		return (
			<div className="ui error message">
				<div className="header">{error}</div>
			</div>
		)
	}
	/*renderInput = ({ input, label, meta }) => {
		const className = `field ${meta.error && meta.touched ? 'error' : ''}`;
		//console.log(input)
		return (
			<div className={className}>
				<label>{label}</label>
				{/!*<input {...input} autoComplete="off"/>*!/}
				<input onChange={input.onChange} value={input.value}/>
				{this.renderError(meta)}
			</div>
		);
	}*/
	renderInput(formProps) {
		console.log(formProps.input)
		return (
			<input {...formProps.input}
				/>
		)
	}

	onSubmit(formValues) {
		//event.preventDefault(formValues);
	}

	render() {
		return (
			<form
				//onSubmit={this.props.handleSubmit(this.onSubmit)}
				className="ui form"
			>
				<Field
					name="title"
					component={this.renderInput}
					label="Enter Title"
				/>
				{/*<Field
					name="description"
					component={this.renderInput}
					label="Enter Description"
				/>*/}
				<button className="ui button primary">Submit</button>
			</form>
		)
	}
}

const validate = (formValues) => {
	const errors = {}

	if (!formValues.title) {
		errors.title = 'You must enter a title';
	}

	if (!formValues.description) {
		errors.description = 'You must enter a description';
	}

	return errors;
}

export default reduxForm({
	form: 'streamCreate',
	//validate
})(StreamCreate);