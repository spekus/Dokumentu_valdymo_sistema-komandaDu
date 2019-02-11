import React, {Component} from 'react';
import DocumentsListSimple from "../../Documents/ReactFragments/AugustasDocumentsList";
import axios from 'axios';
import {Link} from 'react-router-dom';
import DashboardNavigation from './DashBoardElements/DashboardNavigation';


class GenericDashBoard extends Component {
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
        console.log("runing getAllDocuments");
        console.log("adreso pabaiga " + this.props.match.params.id.toUpperCase());
        
        
        
        axios.get('/api/documents/' + this.state.userId + '/documents/' + this.props.match.params.id.toUpperCase())
            .then(response => {
               
                console.log("response from /api/documents/' - " + response);
                this.setState({userDocuments : response.data})
            })
            .catch(err => {
                this.setState({error: err.message})
                console.log("Error from /api/documents/{userIdentifier}/documents - " 
                + err)
            });
    }


    render() {
        const person = (props) => {
            if(this.state.nameOfWindow == "submitted"){
                return (
                    <div>
                <h1>"Hi my name is augustas and this 
                    window is submitted"</h1>
                <DashboardNavigation/>
                </div>)
            }

        }
        return (
            <React.Fragment>
                {/* Dokumentu {this.state.nameOfWindow} */}
                {person()}
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

export default GenericDashBoard;