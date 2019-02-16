import React, {Component} from 'react';
import DocumentsListSimple from "../../Documents/ReactFragments/AugustasDocumentsList";
import axios from 'axios';
import {Link} from 'react-router-dom';
import DashboardNavigation from './DashBoardElements/DashboardNavigation';
import ReactPaginate from 'react-paginate';

class GenericDashBoard extends Component {
    state = { 
        nameOfWindow : 'default',
        userDocuments : [],
        pageCount : 3,
        perPage : 5,
        count: 2,
        offset: 0 //which page

    }
    componentWillUnmount(){
    
    }

    componentWillMount(){
        

    }
    componentDidMount(){
        this.getAllDocuments();
    }

    componentDidUpdate(){
        console.log("window did update");
        if(!(this.state.nameOfWindow == this.props.match.params.id))
        {
        this.setState({nameOfWindow : this.props.match.params.id})
        console.log("state of name of the window was set to - " +
        this.state.nameOfWindow);
        this.getAllDocuments();
        }
        if (this.state.nameOfWindow == '') {
            this.setState({nameOfWindow : this.props.match.params.id})
            console.log("state of name of the window was set to - " +
            this.state.nameOfWindow);
            this.getAllDocuments();
        }
        
    }

    getAllDocuments() {
        let requestPath = "";

        if (this.props.match.params.id.toLowerCase() === "all")
        {
            requestPath = '/api/documents/' + this.props.user.userIdentifier + '/documents/';
        }
        else
        {
            requestPath = '/api/documents/' + this.props.user.userIdentifier + '/documents/' + this.props.match.params.id.toUpperCase();
}

        console.log("getFileList is being run")
        axios({
            method: 'GET',
            url: requestPath,
            params: {
                page: this.state.offset ,
                size: this.state.perPage
            },
            // headers: {'Content-Type': 'application/json;charset=utf-8'}
        })
            .then(response => {
                this.setState({userDocuments : response.data.content})
                // this.setState({pageCount: response.data.totalElements})
                this.setState({pageCount: 
                    Math.ceil(response.data.totalElements 
                        / this.state.perPage)})
                // this.setState({count: this.response.data.totalElements})
                // this.setState({pageCount: Math.ceil(response.meta.total_count / response.meta.limit)})

                // console.log("ammount of total elements" + this.state.count);
            })
            .catch(error => {
                this.setState({error: error.message})
                console.log("error message " + error)
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
        // const person = (props) => {
        //     if(this.state.nameOfWindow == "submitted"){
        //         return (
        //             <div>
        //         <h1>"Hi my name is augustas and this 
        //             window is submitted"</h1>
        //         <DashboardNavigation/>
        //         </div>)
        //     }
        // }
        return (
            <React.Fragment>
                {/* Dokumentu {this.state.nameOfWindow} */}

                <div className="row mt-2">
                    <DashboardNavigation/>
                    
                    <div className='col-lg-12 mt-3'>
                        <DocumentsListSimple list={this.state.userDocuments}/>
                    </div>
                </div>
                <div className='container-fluid mt-5'>
                <div class="row">
                <div className="col-lg-12 my-auto center-block text-center">
                <ReactPaginate 
                previousLabel={'previous'}
                nextLabel={'next'}
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


            </React.Fragment>
        );
    }
}

export default GenericDashBoard;