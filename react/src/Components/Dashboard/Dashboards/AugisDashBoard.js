import React, {Component} from 'react';
import DocumentsListSimple from "../../Documents/ReactFragments/AugustasDocumentsList";
import axios from 'axios';
import {Link} from 'react-router-dom';
import DashboardNavigation from './DashBoardElements/DashboardNavigation';


class AugisDashBoard extends Component {
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
                console.log("response - " + response);
                console.log("response - " + response.data);
                console.log("response title - " + response.data[1].title);
                console.log("response author - " 
                            + response.data[1].author);
                // const dataList =
                // response.data.map((p) => {
                //     return (
                //         <div>
                //     <h3>titile {p.title}</h3>
                //     <h3>author {p.author}</h3>
                //         </div>
                //     );
                // });
                // this.setState({masyvas : dataList})
                // console.log("masyvas - " + dataList[0].author);
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

export default AugisDashBoard;