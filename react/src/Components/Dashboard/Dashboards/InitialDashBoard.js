import React, {Component} from 'react';
import DocumentsListSimple from "../../Documents/ReactFragments/AugustasDocumentsList";
import axios from 'axios';
import {Link} from 'react-router-dom';
import DashboardNavigation from './DashBoardElements/DashboardNavigation';


class InitialDashBoard extends Component {
    state = { 
        userId : 'id123',
        userDocuments : [],
    }
    componentWillMount(){
        this.getAllDocuments();
    }

    getAllDocuments() {
        console.log("runing getAllDocuments");
        axios.get('/api/documents/' + this.state.userId + '/documents')
            .then(response => {

                console.log("response from /api/documents/ - " + response.data);
                // console.log("response title - " + response.data[1].title);
                this.setState({userDocuments : response.data})
            })
            .catch(err => {
                this.setState({error: err.message})
                console.log("Error from /api/documents/{userIdentifier}/documents - " 
                + err)
            });
    }


    render() {
        return (
            <React.Fragment>
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

export default InitialDashBoard;