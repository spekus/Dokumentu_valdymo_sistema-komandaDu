import React, {Component} from 'react';
import DocumentsListSimple from "./DashBoardElements/AugustasDocumentsList";
import axios from 'axios';
import {Link} from 'react-router-dom';
import DashboardNavigation from './DashBoardElements/DashboardNavigation';


class ToApproveDashboard extends Component {
    state = {
        nameOfWindow: 'default',
        userIdentifier: '',
        userDocuments: [],
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
                if (response.data.username != null) {
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


    getDocumentsToApprove = () => {
        axios.get('/api/users/user/get-documents-to-approve')
            .then(response => {
                this.setState({userDocuments: response.data});
            })
            .catch(error => {
                console.log("Klaida is getDocumentsToApprove metodo - " + error.message)
            })
    }

    // getDocumentsToApprove = () => {
    //     axios({
    //         method: 'get',
    //         url: '/api/users/user/get-documents-to-approve',
    //         params: {
    //             userIdentifier: this.state.userIdentifier
    // },
    //     headers: {'Content-Type': 'application/json;charset=utf-8'}
    //
    // })
    //             .then(response => {
    //             this.setState({userDocuments: response.data});
    //         })
    //         .catch(error => {
    //             console.log("Klaida is getDocumentsToApprove metodo - " + error.message)
    //         })
    // }




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


    render() {

        return (
            <React.Fragment>
                {/* Dokumentu {this.state.nameOfWindow} */}
                <div className="row mt-2">
                    <DashboardNavigation/>

                    <div className='col-lg-12 mt-3'>
                          {/*{this.state.userDocuments.map(item => (*/}
                                                {/*<p value={item.title}>{item.title}</p>*/}
                                            {/*))}*/}
                        {/*/!*<p>Hello {this.state.userDocuments[0]}</p>*!/*/}
                        <DocumentsListSimple list={this.state.userDocuments}/>
                    </div>
                </div>

            </React.Fragment>
        );
    }
}

export default ToApproveDashboard;