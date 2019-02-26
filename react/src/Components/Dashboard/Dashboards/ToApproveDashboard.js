import React, {Component} from 'react';
import DocumentsListSimple from "./ElementsOfDashBoard/DocumentsList";
import axios from 'axios';
import ReactPaginate from 'react-paginate';
import DashboardNavigation from './ElementsOfDashBoard/DashboardNavigation';

class ToApproveDashboard extends Component {
    state = {
        nameOfWindow: 'default',
        userIdentifier: '',
        userDocuments: [],

        // used for paging
        pageCount : 3,
        perPage : 7,
        offset: 0 //identifies which page is used
    }


    componentDidMount = () => {
        this.getWhoAmI();
        // this.getDocumentsToApprove();
        // this.setState({userId: this.props.user.userIdentifier});
        console.log("userID" + this.state.userIdentifier)
    }

    // componentDidUpdate =(props) =>{
    //     console.log("window did update");
    //      console.log("userID" + this.state.userId)
    //     if(!(this.state.nameOfWindow == this.props.match.params.id)){
    //     this.setState({nameOfWindow : this.props.match.params.id})
    //     console.log("state of name of the window was set to - " +
    //     this.state.nameOfWindow);
    //     this.getDocumentsToApprove();
    //     }
    // }

    getWhoAmI = () => {
        axios.get('/api/users/whoami')
            .then(response => {
                if (response.data.username !== null) {
                    this.setState({userIdentifier: response.data.userIdentifier});
                    console.log("getWhoAmI - " + this.state.userIdentifier);
                    this.getDocumentsToApprove();
                }

            })
            .catch(error => {
                console.log("Error from getWhoAmI");
                console.log(error);
            })
    }


    // getDocumentsToApprove = () => {
    //     axios.get('/api/users/user/get-documents-to-approve')
    //         .then(response => {
    //             this.setState({userDocuments: response.data});
    //         })
    //         .catch(error => {
    //             console.log("Klaida is getDocumentsToApprove metodo - " + error.message)
    //         })
    // }


    getDocumentsToApprove = () => {
        axios({
            method: 'get',
            url: '/api/users/user/get-documents-to-approve',
            params: {
                page: this.state.offset ,
                size: this.state.perPage
    },
        headers: {'Content-Type': 'application/json;charset=utf-8'}
    
    })
                .then(response => {
                    //we use response.data.content, becouse files re under content
                    //data. allso holds paging information
                this.setState({userDocuments: response.data.content});
                this.setState({pageCount: 
                    Math.ceil(response.data.totalElements 
                        / this.state.perPage)})
                        console.log("totalElements - = " + response.data.totalElements)
            })
            .catch(error => {
                console.log("Klaida is getDocumentsToApprove metodo - " + error.message)
            })
    }




    // getDocumentsToApprove = (props) => {
    //     // this.setState({userIdentifier: this.props.user.userIdentifier});
    //     var params = new URLSearchParams();
    //     params.append('userIdentifier', this.props.user.userIdentifier);
    //     console.log("running getAllDocuments");
    //     axios.get('/api/usergroup/getDocumentsToApprove', params)
    //         .then(response => {
    //             this.setState({userDocuments: response.data});
    //         })
    //         .catch(error => {
    //             console.log("Klaida is getDocumentsToApprove metodo - " + error.message)
    //         })
    // }
    handlePageClick = data => {
        let selected = data.selected;
        let offset = Math.ceil(selected);
    
        this.setState({ offset: offset }, () => {
          this.getDocumentsToApprove();
        });
    };


    render() {

        return (
            <React.Fragment>
                {/* Dokumentu {this.state.nameOfWindow} */}
                <div className="row mt-2">
                    <DashboardNavigation/>

                    <div className='col-lg-12 mt-3 container'>
                          {/*{this.state.userDocuments.map(item => (*/}
                                                {/*<p value={item.title}>{item.title}</p>*/}
                                            {/*))}*/}
                        {/*/!*<p>Hello {this.state.userDocuments[0]}</p>*!/*/}
                        <DocumentsListSimple list={this.state.userDocuments}/>
                    </div>
                </div>

                {/* pagination */}
                <div className='container mt-5'>
                <div className="row">
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

export default ToApproveDashboard;