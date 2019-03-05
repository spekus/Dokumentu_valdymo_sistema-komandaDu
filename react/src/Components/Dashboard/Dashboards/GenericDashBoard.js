import React, {Component} from 'react';
import DocumentsList from "./ElementsOfDashBoard/DocumentsList";
import axios from 'axios';
import DashboardNavigation from './ElementsOfDashBoard/DashboardNavigation';
import ReactPaginate from 'react-paginate';
import {showErrorObject} from "../../UI/MainModalError";


class GenericDashBoard extends Component {
    state = { 
        nameOfWindow : 'default',
        userDocuments : [],
        firstOpen : true,
        // used for paging
        pageCount : 3,
        perPage : 7,
        offset: 0 //identifies which page is used
    }

    componentDidMount(){
        this.getAllDocuments();
    }

    componentDidUpdate(){
        // LABAI LABAI LABAI BLOGAS KODAS, bet lyk veikia
        // these are just to make sure new data is leaded when going between dashboards
        console.log("window did update");
        if(!(this.state.nameOfWindow === this.props.match.params.id))
        {
        this.setState({nameOfWindow : this.props.match.params.id})
            // this is just for performance, to make sure that there would be double 
            // calling of axios then you first update page
        if(this.state.firstOpen === false){
        this.getAllDocuments();
            }
            this.setState({firstOpen : false})
        }
        if (this.state.nameOfWindow === '') {
            this.setState({nameOfWindow : this.props.match.params.id})
            if(this.state.firstOpen === false){
                this.getAllDocuments();
                    }
            this.setState({firstOpen : false})
        }
        
    }


    getAllDocuments() {
        // console.log("running getAllDocuments");
        // console.log("adreso pabaiga " + this.props.match.params.id.toUpperCase());
        
        let requestPath = "";

        if (this.props.match.params.id.toLowerCase() === "all")
        {
            requestPath = '/api/users/user/documents';
        }
        else
        {
             requestPath = '/api/users/user/documents/' + this.props.match.params.id.toUpperCase();
        }

        console.log("getFileList is being run");
        axios.get(requestPath,{params: {
                page: this.state.offset ,
                size: this.state.perPage
            }})
            .then(response => {
                //  kai gavome dokumentu masyva, mappinam i nauja masyva,
                // kur visi laukai tokie patys ( ... operatorius), bet overraidinam postedDate
                // kuris dabar tampa javascriptiniu Date tipo objektu, sukurtu is turimos datos
                let userDocuments = response.data.content.map(document => {
                    return ({
                        ...document,
                        postedDate: new Date(document.postedDate),
                        approvedDate: new Date(document.approvalDate),
                        rejectedDate: new Date(document.rejectedDate)
                    })
                });

                this.setState({userDocuments : userDocuments})
                this.setState({pageCount: 
                    Math.ceil(response.data.totalElements 
                        / this.state.perPage)})
            })
            .catch(error => {
                this.setState({error: error.message})
                console.log("error message " + error);
                showErrorObject(error);
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
                {/* Dokumentu {this.state.nameOfWindow} */}
                <div className="container">

                <div className="row mt-2">
                    <DashboardNavigation/>
                    
                    <div className='col-lg-12 mt-3 p-3 mb-5 bg-white rounded borderMain'>
                        <DocumentsList list={this.state.userDocuments}/>
                    </div>
                </div>

                {/* pagination */}
                <div className='container-fluid mt-2'>
                <div className="row">
                <div className="col-lg-12 my-auto center-block text-center">
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
            </React.Fragment>
        );
    }
}

export default GenericDashBoard;