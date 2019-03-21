import React, { Component } from 'react';
import axios from 'axios'
import uuid from 'uuid';
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


    handleChangeInput = (event) => this.setState({ [event.target.name]: event.target.value });


    handleSubmit = (event) => {
        event.preventDefault();

        var newUser = {
            username: this.state.username,
            firstname: this.state.firstname,
            lastname: this.state.lastname,
            password: this.state.password,
        };



        if (this.state.editmode && this.state.password.length === 0) {
            axios({
                method: 'post',
                url: '/api/users/' + this.state.username,
                params: {
                    firstname: newUser.firstname,
                    lastname: newUser.lastname
                },
                headers: { 'Content-Type': 'application/json;charset=utf-8' }


            })
                .then(response => {
                    this.props.afterSubmit(newUser.username);
                })

        } else if (this.state.editmode && this.state.password.length > 0) {

            axios({
                method: 'post',
                url: '/api/users/' + this.state.username,
                params: {
                    firstname: newUser.firstname,
                    lastname: newUser.lastname,
                }
                ,
                headers: { 'Content-Type': 'application/json;charset=utf-8' }


            })
                .then(
                    axios({
                        method: 'post',
                        url: '/api/users/' + this.state.username + "/password",
                        params: {
                            password: newUser.password
                        }
                        ,
                        headers: { 'Content-Type': 'application/json;charset=utf-8' }


                    })
                )
        } else {
            axios.post('/api/users', newUser)
                .then(response => {

                    this.setState(this.emptyState);
                    this.props.afterSubmit(newUser.username);

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

                    <div className="row">
                        <div className="col-md-1"></div>
                        <div className="col-md-10">
                            <form className="col-md-11" onSubmit={this.handleSubmit}>
                                <div className="form-group">
                                    <label>Vardas</label>
                                    <input type="text" className="form-control" id={uuid()}
                                        minLength="2"
                                        maxLength="50"
                                        pattern="^([a-zA-ąĄčČęĘėĖįĮšŠųŪžŽ]+[,.]?|[A-Za-z]+['-]?)+$"
                                        title="Only letters should be provided!"
                                        placeholder="Įveskite darbuotojo vardą" name="firstname"
                                        value={this.state.firstname}
                                        onChange={this.handleChangeInput} required />
                                </div>

                                <div className="form-group">
                                    <label>Pavardė</label>
                                    <input type="text" className="form-control" id={uuid()}
                                        minLength="2"
                                        maxLength="50"
                                        pattern="^([a-zA-ąĄčČęĘėĖįĮšŠųŪžŽ]+[,.]?|[A-Za-z]+['-]?)+$"
                                        title="Only letters should be provided!"
                                        placeholder="Įveskite darbuotojo pavardę" name="lastname"
                                        value={this.state.lastname}
                                        onChange={this.handleChangeInput} required />
                                </div>
                                {this.props.editmode ?
                                    <div className="form-group">
                                        <label>Naudotojo vardas</label>
                                        <input type="text" className="form-control" id={uuid()}
                                            readOnly
                                            minLength="2"
                                            maxLength="50"
                                            pattern="^([a-zA-ąĄčČęĘėĖįĮšŠųŪžŽ]+[,.]?|[A-Za-z0-9]+['-]?)+$"
                                            title="Only letters and numbers should be provided!"
                                            placeholder="Įveskite vartotojo prisijungimo vardą" name="username"
                                            value={this.state.username}
                                            onChange={this.handleChangeInput}
                                            autoComplete="off"
                                            required />
                                    </div>
                                    :
                                    <div className="form-group">
                                        <label>Naudotojo vardas</label>
                                        <input type="text" className="form-control" id={uuid()}
                                            minLength="2"
                                            maxLength="50"
                                            pattern="^([a-zA-ąĄčČęĘėĖįĮšŠųŪžŽ]+[,.]?|[A-Za-z0-9]+['-]?)+$"
                                            title="Only letters and numbers should be provided!"
                                            placeholder="Įveskite vartotojo prisijungimo vardą" name="username"
                                            value={this.state.username}
                                            onChange={this.handleChangeInput}
                                            autoComplete="off"
                                            required />
                                    </div>
                                }

                                <label htmlFor="inputPassword5">Slaptažodis</label>

                                {this.props.editmode ?
                                    <input type="password" id={uuid()} className="form-control"
                                        minLength="8"
                                        maxLength="20"
                                        pattern="^([a-zA-ąĄčČęĘėĖįĮšŠųŪžŽ]+[,.]?|[A-Za-z0-9]+['-]?)+$"
                                        title="Password must be 8-20 symbols length!"
                                        value={this.state.password}
                                        aria-describedby="passwordHelpBlock" onChange={this.handleChangeInput}
                                        autoComplete="off"
                                        name="password"
                                    />
                                    :
                                    <input type="password" id={uuid()} className="form-control"
                                        minLength="8"
                                        maxLength="20"
                                        pattern="^([a-zA-ąĄčČęĘėĖįĮšŠųŪžŽ]+[,.]?|[A-Za-z0-9]+['-]?)+$"
                                        title="Password must be 8-20 symbols length!"
                                        value={this.state.password}
                                        aria-describedby="passwordHelpBlock" onChange={this.handleChangeInput}
                                        name="password"
                                        autoComplete="off"
                                        required
                                    />
                                }
                                <small id={uuid()} className="form-text text-muted">
                                    Slaptažodis privalo būti 8-20 simbolių ilgio.
                                </small>
                                <div className="text-center">
                                    <button type="submit" className="btn button1 my-4">Išsaugoti</button>
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