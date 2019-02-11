import React, {Component} from 'react';
import DocumentsListSimple from "../../Documents/ReactFragments/AugustasDocumentsList";
import axios from 'axios';
import {Link} from 'react-router-dom';
import DashboardNavigation from './DashBoardElements/DashboardNavigation';


class ToAprooveDashboard extends Component {
    state = { 
        nameOfWindow : 'default',
        userId : 'id123',
        userDocuments : [],
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
        if(!(this.state.nameOfWindow == this.props.match.params.id)){
        this.setState({nameOfWindow : this.props.match.params.id})
        console.log("state of name of the window was set to - " +
        this.state.nameOfWindow);
        this.getAllDocuments();
        }
        
    }

    getAllDocuments() {
        console.log("runing getAllDocuments");
        axios({
            method: 'GET',
            url: '/api/usergroup/getDocumentsToApprove',
            params: {
                userIdentifier: (this.state.userId)
            },
            
            // headers: {'Content-Type': 'application/json;charset=utf-8'}
        }).then(response => {
        this.setState({userDocuments : response.data})
        })
            .catch(err => {
                this.setState({error: err.message})
                console.log("Error from /api/usergroup/' + 'getDocumentsToApprove/' - " 
                + err)
            });
    }


    render() {
        // var objectsToDisplay =
        //     return (
        //     <DocumentsListSimple list={this.state.userDocuments}/>
        //     )

        

        return (
            <React.Fragment>
                {/* Dokumentu {this.state.nameOfWindow} */}
                <div className="row mt-2">
                    <DashboardNavigation/>
                    
                    <div className='col-lg-12 mt-3'>
                        <DocumentsListSimple list={this.state.userDocuments}/>
                    </div>
                </div>

            </React.Fragment>
        );
    }
}

export default ToAprooveDashboard;