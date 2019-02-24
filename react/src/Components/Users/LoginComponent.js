import React, {Component} from 'react';
import axios from 'axios';
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

    componentDidMount() {
        $('#loginModal').modal('show');
    }

    componentWillUnmount() {
        $('#loginModal').modal('hide');
    }

    render() {
        return (
            <React.Fragment>
                <div className="modal" id='loginModal' data-backdrop="static" data-keyboard="false">
                    <div className="modal-dialog modal-dialog-centered">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h4 className="modal-title">Prašome prisijungti</h4>
                            </div>
                            <div className="modal-body">
                                {this.state.errorText === '' ? '' :
                                    <div id='loginAlert' className="alert alert-warning show"
                                         role="alert">
                                        <h5>Klaida!</h5>{this.state.errorText}
                                    </div>}
                                <form>
                                    <div className="form-group">
                                        <input type="text"
                                               name='username'
                                               value={this.state.username}
                                               placeholder="Vartotojo vardas"
                                               onChange={this.handleChangeInput}/>
                                    </div>
                                    <div className="form-group">
                                        <input type="password"
                                               name='password'
                                               value={this.state.password}
                                               placeholder="Slaptažodis"
                                               onChange={this.handleChangeInput}/>
                                    </div>
                                    <button type="submit" value="username" className="btn btn-outline-info my-2 "
                                            onClick={this.handleSubmit}>Prisijungti
                                    </button>

                                </form>
                            </div>
                        </div>
                    </div>
                </div>

            </React.Fragment>
        )
    }
}

