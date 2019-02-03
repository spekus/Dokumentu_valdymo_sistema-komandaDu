import React, {Component} from 'react';

class NewUserForm extends Component {
    render() {
        return (
            <React.Fragment>
                <div>
                    <h4 className="my-4" align="center">
                        Naujo vartotojo registravimas
                    </h4>
                    <div className="row">
                        <div className="col-md-1"></div>
                        <div className="col-md-10">

                            <form className="col-md-11">

                                <div className="form-group">
                                    <label htmlFor="exampleFormControlInput1">Vardas</label>
                                    <input type="text" className="form-control" id="exampleFormName"
                                           placeholder="Įveskite darbuotojo vardą"/>
                                </div>

                                <div className="form-group">
                                    <label htmlFor="exampleFormControlInput1">Pavardė</label>
                                    <input type="text" className="form-control" id="exampleFormSurname"
                                           placeholder="Įveskite darbuotojo pavardę"/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="exampleFormControlInput1">Mob. telefono numeris</label>
                                    <input type="text" className="form-control" id="exampleFormPhone"
                                           placeholder="+370 XXX XXXXX"/>
                                </div>

                                <div className="form-group">
                                    <label htmlFor="exampleFormControlInput1">El.pašto adresas</label>
                                    <input type="email" className="form-control" id="exampleFormEmail"
                                           placeholder="name@example.com"/>
                                </div>

                                <div className="form-group">
                                    <label htmlFor="exampleFormControlSelect2">Darbuotojų grupės</label>
                                    <select multiple className="form-control" id="exampleFormControlSelect2">
                                        <option>1.Administracija</option>
                                        <option>2.Buhalterija</option>
                                        <option>3.Darbuotojai</option>
                                        <option>4.Projektų vadovai</option>
                                        <option>5.Praktikantai</option>
                                    </select>
                                    <small id="passwordHelpBlock" className="form-text text-muted">
                                        Galima pasirinkti daugiau nei vieną grupę.
                                    </small>
                                </div>

                                <label htmlFor="inputPassword5">Password</label>
                                <input type="password" id="inputPassword5" className="form-control"
                                       aria-describedby="passwordHelpBlock"/>
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