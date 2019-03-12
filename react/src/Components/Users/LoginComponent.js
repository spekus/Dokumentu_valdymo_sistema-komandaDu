import React, {Component} from 'react';
import axios from 'axios';
// import $ from 'jquery';
import "../../App.css"
import "./LoginComponent.css"
// import * as uuid from "uuid";
import $ from 'jquery';



export default class Login extends Component {
    state = {
        username: '',
        password: '',
        errorText: ''
    }

    tryLogin() {
        this.setState({errorText: ''});
        axios.post('/login', null, {
            params: {
                username: this.state.username,
                password: this.state.password
            }
        })
            .then(response => {
                // mes prisijungeme, todel dabar galime suzinoti naudotojo informacija
                // ta padarysim iskvieciant callback onLogin
                this.props.onLogin();
            })
            .catch(error => {
                let errorText = (typeof (error.response.data) === 'string') ? error.response.data : error.response.data.message;
                this.setState({errorText: errorText});
            })
    }

    handleChangeInput = (event) => this.setState({[event.target.name]: event.target.value});

    handleSubmit = (event) => {
        event.preventDefault();
        this.tryLogin();

    }

    render() {
        return (
            <div className='loginBackground'>
                <div className='loginDialog'>
                    <div className="loginHeader">
                        <h4>Prašome prisijungti</h4>
                    </div>
                    {this.state.errorText === '' ? null :
                        <div className='loginAlert'>
                            <h5>Klaida!</h5>{this.state.errorText}
                        </div>
                    }
                    {this.props.loginError ?
                        <div className='loginAlert'>
                            <h5>Prisijungimo klaida!</h5>{this.props.loginError}
                        </div>
                    :null}
                    <form>
                        <div className="form-group">
                            <input type="text"
                                   name='username'
                                   value={this.state.username}
                                   placeholder="Naudotojo vardas"
                                   onChange={this.handleChangeInput}/>
                        </div>
                        <div className="form-group">
                            <input type="password"
                                   name='password'
                                   value={this.state.password}
                                   placeholder="Slaptažodis"
                                   onChange={this.handleChangeInput}/>
                        </div>
                        <button type="submit" value="username" className="btn button1"
                                onClick={this.handleSubmit}>Prisijungti
                        </button>
                    </form>
                </div>
            </div>
        )
    }
}

