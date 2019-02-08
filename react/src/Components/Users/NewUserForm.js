import React, {Component} from 'react';
import axios from 'axios'
import $ from "jquery";

class NewUserForm extends Component {

    constructor() {
        super();
        this.state = this.emptyState
    }

    emptyState = {
        editmode: false,
        userIdentifier: '',
        username: '',
        firstname: '',
        lastname: '',
        password: ''
    }


    handleChangeInput = (event) => this.setState({[event.target.name]: event.target.value});


    handleSubmit = (event) => {
        event.preventDefault();
        console.log(this.state);
        var newUser = {
            userIdentifier: this.state.userIdentifier,
            username: this.state.username,
            firstname: this.state.firstname,
            lastname: this.state.lastname,
            password: this.state.password,
        };


        console.log("New User: " + newUser);

        if (this.state.editmode) {
            axios.put('/api/users/' + this.state.userIdentifier)
                .then()
        } else {

            axios.post('/api/users/', newUser)
                .then(response => {
                    console.log(response);
                    this.setState(this.emptyState);

                })
                .catch(error => {
                    console.log("Klaida is addNewUser: " + error.response.data.message);
                });
        }
    };

    componentWillReceiveProps(nextProps, nextContext) {
        if (nextProps.editmode) {
            this.setState(nextProps);
        }
    }

    render() {
        return (
            <React.Fragment>
                <div>
                    <h4 className="my-4" align="center">
                        {this.props.editmode ? "Naudotojo redagavimas" : "Naujo vartotojo registravimas"}
                    </h4>
                    <div className="row">
                        <div className="col-md-1"></div>
                        <div className="col-md-10">

                            <form className="col-md-11" onSubmit={this.handleSubmit}>
                                <div className="form-group">
                                    <label htmlFor="exampleFormControlInput1">Vardas</label>
                                    <input type="text" className="form-control" id="exampleFormName"
                                           placeholder="Įveskite darbuotojo vardą" name="firstname"
                                           value={this.state.firstname}
                                           onChange={this.handleChangeInput}/>
                                </div>

                                <div className="form-group">
                                    <label htmlFor="exampleFormControlInput1">Pavardė</label>
                                    <input type="text" className="form-control" id="exampleFormSurname"
                                           placeholder="Įveskite darbuotojo pavardę" name="lastname"
                                           value={this.state.lastname}
                                           onChange={this.handleChangeInput}/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="exampleFormControlInput1">Username</label>
                                    <input type="text" className="form-control" id="exampleFormUsername"
                                           placeholder="Įveskite vartotojo prisijungimo vardą" name="username"
                                           value={this.state.username}
                                           onChange={this.handleChangeInput}/>
                                </div>
                                {this.props.editmode ? '' :
                                <div className="form-group">
                                    <label htmlFor="exampleFormControlInput1">Identifikatorius</label>
                                    <input type="text" className="form-control" id="exampleFormIdentifier"
                                           placeholder="Įveskite vartotojo identifikatorių" name="userIdentifier"
                                           value={this.state.userIdentifier}
                                           onChange={this.handleChangeInput}/>
                                </div>}

                                {/*<div className="form-group">*/}
                                {/*<label htmlFor="exampleFormControlSelect2">Darbuotojų grupės</label>*/}
                                {/*<select multiple className="form-control" id="exampleFormControlSelect2"*/}
                                {/*value="" onChange={this.state.handleChangeSelect} name="group">*/}
                                {/*{this.state.availableGroups.map(group =>(*/}
                                {/*<option value={group.title}>{group.title}</option>*/}
                                {/*))}*/}
                                {/*</select>*/}
                                {/*<small id="passwordHelpBlock" className="form-text text-muted">*/}
                                {/*Galima pasirinkti daugiau nei vieną grupę.*/}
                                {/*</small>*/}
                                {/*</div>*/}

                                <label htmlFor="inputPassword5">Password</label>
                                <input type="password" id="inputPassword5" className="form-control"
                                       value={this.state.password}
                                       aria-describedby="passwordHelpBlock" onChange={this.handleChangeInput}
                                       name="password"/>
                                <small id="passwordHelpBlock" className="form-text text-muted">
                                    Your password must be 8-20 characters long, contain letters and numbers, and must
                                    not
                                    contain spaces, special characters, or emoji.
                                </small>

                                <div className="text-center">
                                    <button type="submit" className="btn btn-danger my-4">Išsaugoti</button>
                                </div>


                            </form>
                        </div>
                    </div>


                </div>
            </React.Fragment>
        );
    }
}

export default NewUserForm;