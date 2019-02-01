import React, {Component} from 'react';
import NavLink from "react-router-dom/es/NavLink";
import {Link} from "react-router-dom";

class UserAdministration extends Component {


    render() {
        return (
            <React.Fragment>
                <div>
                    <h4 className="my-4" align="center">
                         Vartotojų administravimas
                    </h4>

                    <div className="form-group col-md-8 my-5">
                        <label htmlFor="exampleFormControlInput1">Vartotojo paieška</label>
                        <div className="row">
                            <div className="col-md-8 input-group">
                                <div className="input-group-prepend">
                                <span className="input-group-text" id="basic-addon1">🔎</span>
                            </div>
                                <input className="form-control mr-sm-2" type="search"
                                       placeholder="Įveskite vartotojo identifikatorių"
                                       aria-label="Search" aria-describedby="basic-addon1"/>
                            </div>
                            <div className="col-md-2">
                                <button className="btn btn-danger my-2 my-sm-0" type="submit">Ieškoti</button>
                            </div>
                            <div className="col-md-2">
                                <button className="btn btn-outline-danger my-2 my-sm-0 buttonXL" type="submit" onClick={()=>{this.props.history.push("/user-registration")}}>Registruoti naują vartotoją</button>
                            </div>
                        </div>
                    </div>
                    {/*/!*Galima padaryti taip ka daro Link viduje. Rankiniu budu papushinti i History *!/*/}
                    {/*<button className="btn btn-outline-danger my-2 my-sm-0" onClick={()=>{this.props.history.push("/profile")}}>Jusu profilis</button><br/>*/}

                    {/*/!*NavLink - tai yra kaip Link, bet moka prideti active klase, priklausomai nuo kelio*!/*/}
                    {/*<NavLink to="/profile">Jusu profilis</NavLink><br/>*/}

                    {/*/!*reikia naudoti Link*!/*/}
                    {/*<Link to="/profile">Jusu profilis</Link>*/}

                </div>
            </React.Fragment>

        )
            ;
    }
}

export default UserAdministration;