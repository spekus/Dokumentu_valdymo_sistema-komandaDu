import React, { Component } from 'react';
import axios from "axios";
import { showErrorObject } from "../UI/MainModalError";
import '../../App.css';
import uuid from 'uuid';
import Link from "react-router-dom/es/Link";
import { translateAction } from "./AuditLogTranslate";
import ReactPaginate from "react-paginate";


class AuditLog extends Component {
    state = {
        searchField: '',
        allEntries: [],

        pageCount: 0,
        perPage: 10,
        offset: 0,
    }

    handleChangeInput = (event) => this.setState({ [event.target.name]: event.target.value });

    componentDidMount() {
        this.getAllAuditEntries();
    }

    handleSearch = () => {

        if (this.state.searchField.length === 0) {
            this.setState({ allEntries: [] })
            this.setState({ offset: 0 }, () => {
                this.getAllAuditEntries();


            })

        } else {
            this.setState({ allEntries: [] })
            this.setState({ offset: 0 }, () => {
                this.getFilteredAuditEntries();


            })

        }
    }


    getAllAuditEntries = () => {
        axios({
            method: 'get',
            url: '/api/auditentries',

            params: {
                page: this.state.offset,
                size: this.state.perPage
            },
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
        })
            .then(response => {
                if (response.data.content.length > 0) {
                    this.setState({ allEntries: response.data.content });

                }

                this.setState({ pageCount: Math.ceil(response.data.totalElements / this.state.perPage) });

            }
            )
            .catch(error => {
                showErrorObject(error);
            })
    }

    getFilteredAuditEntries = () => {
        axios.get('/api/auditentries/search', {
            params:
            {
                page: this.state.offset,
                size: this.state.perPage,
                criteria: this.state.searchField
            }
        })

            .then(response => {
                ;
                if (response.data.content.length > 0) {
                    this.setState({ allEntries: response.data.content });

                }

                this.setState({ pageCount: Math.ceil(response.data.totalElements / this.state.perPage) });


            })
            .catch(error => {
                showErrorObject(error);
            })

    }


    handlePageClick = data => {
        let selected = data.selected;
        let offset = Math.ceil(selected);

        if (this.state.searchField.length === 0) {
            this.setState({ offset: offset }, () => {
                this.getAllAuditEntries();


            })

        } else {
            this.setState({ offset: offset }, () => {
                this.getFilteredAuditEntries();

            })

        }
    }


    handleKeyPress = (event) => {
        if (event.key === 'Enter') {
            this.setState({ [event.target.name]: event.target.value });
            this.handleSearch();
        }
    }

    render() {
        return (
            <div className="container">


                <div className='mainelement borderMain my-3' style={{ 'width': '100%' }}>


                    <div className="form-group col-md-10 my-3">
                        <label>Audito įrašo paieška</label>
                        <div className="row">
                            <div className="col-md-10 input-group">
                                <div className="input-group-prepend">
                                    <span className="input-group-text" id="basic-addon1" role="img"
                                        aria-label="Search">🔎</span>
                                </div>
                                <input className="form-control mr-sm-2" type="search"
                                    placeholder="Įveskite naudotojo vardą, pavardę, reg. vardą (username) arba objekto ID"
                                    aria-label="Search" aria-describedby="basic-addon1"
                                    value={this.state.searchField}
                                    name="searchField"
                                    onChange={this.handleChangeInput}
                                    onKeyPress={this.handleKeyPress} />
                            </div>
                            <div className="col-md-1">
                                <button className="btn button2 my-2 my-sm-0 button1" type="submit"
                                    onClick={this.handleSearch}
                                >Ieškoti
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <div className='mainelement borderMain' style={{ 'width': '100%' }}>
                    <table className="table table-bordered table-hover table-sm my-3">
                        <thead>
                            <tr>
                                <th>Data</th>
                                <th>Naudotojas</th>
                                <th>Vardas</th>
                                <th>Pavardė</th>
                                <th>Veiksmas</th>
                                <th>Objektas</th>
                            </tr>
                        </thead>
                        <tbody>
                            {this.state.allEntries.map(entry => {
                                let objectDetails = entry.objectIdentifier;

                                if (entry.objectType === "DOCUMENT") {
                                    let address = "/documents/" + entry.objectIdentifier;
                                    objectDetails = <Link to={address}>Atidaryti</Link>
                                }

                                return (
                                    <tr key={uuid()}>
                                        <td>{entry.date}</td>
                                        <td>{entry.username}</td>
                                        <td>{entry.firstname}</td>
                                        <td>{entry.lastname}</td>
                                        <td>{translateAction(entry.action)}</td>

                                        <td>{objectDetails}</td>
                                    </tr>)
                            })}
                        </tbody>
                    </table>
                </div>

                <div className='container mt-5'>
                    <div className="row">
                        <div className="col-lg-12 my-auto center-block text-center">
                            <ReactPaginate
                                forcePage={this.state.offset}
                                previousLabel={'ankstesnis puslapis'}
                                nextLabel={'kitas puslapis'}
                                breakLabel={'...'}
                                breakClassName={'break-me'}
                                pageCount={this.state.pageCount}
                                marginPagesDisplayed={3}
                                pageRangeDisplayed={10}
                                onPageChange={this.handlePageClick}
                                containerClassName={'pagination'}
                                subContainerClassName={'pagesPagination'}
                                activeClassName={'active'}
                            />
                        </div>
                    </div>
                </div>


            </div>
        );
    }
}

export default AuditLog;