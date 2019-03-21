import React, { Component } from 'react';
import DocumentsList from "./ElementsOfDashBoard/DocumentsList";
import axios from 'axios';
import DashboardNavigation from './ElementsOfDashBoard/DashboardNavigation';
import ReactPaginate from 'react-paginate';
import { showErrorObject } from "../../UI/MainModalError";


class GenericDashBoard extends Component {
    state = {
        nameOfWindow: 'default',
        userDocuments: [],

        paginationIncarnation: 1,

        pageCount: 3,
        perPage: 7,
        offset: 0
    }

    componentDidMount() {
        this.getAllDocuments();
    }

    componentDidUpdate() {
        if (!(this.state.nameOfWindow === this.props.match.params.id)) {

            this.setState({ paginationIncarnation: this.state.paginationIncarnation + 1 });
            this.setState({ nameOfWindow: this.props.match.params.id })
            this.getAllDocuments();
        }

        if (this.state.nameOfWindow === '') {
            this.setState({ nameOfWindow: this.props.match.params.id })
            this.getAllDocuments();
        }

    }


    getAllDocuments() {
        this.setState({ offset: 0 })
        let requestPath = "";

        if (this.props.match.params.id.toLowerCase() === "all") {
            requestPath = '/api/users/user/documents';
        }
        else {
            requestPath = '/api/users/user/documents/' + this.props.match.params.id.toUpperCase();
        }

        axios.get(requestPath, {
            params: {
                page: this.state.offset,
                size: this.state.perPage
            }
        })
            .then(response => {
                let userDocuments = response.data.content.map(document => {
                    return ({
                        ...document,
                        postedDate: new Date(document.postedDate),
                        approvedDate: new Date(document.approvalDate),
                        rejectedDate: new Date(document.rejectedDate)
                    })
                });
                this.setState({ userDocuments: userDocuments })
                this.setState({
                    pageCount:
                        Math.ceil(response.data.totalElements
                            / this.state.perPage)
                })
            })
            .catch(error => {
                this.setState({ error: error.message })
                console.log("error message " + error);
            })
    }
    handlePageClick = data => {
        let selected = data.selected;
        let offset = Math.ceil(selected);

        this.setState({ offset: offset }, () => {
            this.getAllDocuments();
        });
    };


    render() {
        return (
            <React.Fragment>
                <div className="container">

                    <div className="row mt-2">
                        <DashboardNavigation />

                        <div className='col-lg-12 mt-3 p-3 mb-5 bg-white rounded borderMain'>
                            <DocumentsList list={this.state.userDocuments} />
                        </div>
                    </div>

                    <div className='container-fluid mt-2'>
                        <div className="row">
                            <div className="col-lg-12 my-auto center-block text-center">
                                <div key={this.state.paginationIncarnation}>
                                    <ReactPaginate
                                        previousLabel={'ankstesnis puslapis'}
                                        nextLabel={'kitas puslapis'}
                                        breakLabel={'...'}
                                        breakClassName={'break-me'}
                                        pageCount={this.state.pageCount}
                                        marginPagesDisplayed={2}
                                        pageRangeDisplayed={5}
                                        onPageChange={this.handlePageClick}
                                        containerClassName={'pagination'}
                                        subContainerClassName={'pagesPagination'}
                                        activeClassName={'active'}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </React.Fragment>
        );
    }
}

export default GenericDashBoard;