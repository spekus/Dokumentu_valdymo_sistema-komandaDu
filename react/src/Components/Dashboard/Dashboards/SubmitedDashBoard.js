import React, {Component} from 'react';
import DocumentsListSimple from "../../Documents/ReactFragments/AugustasDocumentsList";
import axios from 'axios';
import {Link} from 'react-router-dom';
import DashboardNavigation from './DashBoardElements/DashboardNavigation';


class SubmitedDashBoard extends Component {
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
        console.log("adreso pabaiga " + this.props.match.params.id.toUpperCase());
        axios.get('/api/documents/' + this.state.userId + '/documents/' + this.props.match.params.id.toUpperCase())
            .then(response => {
               
                console.log("response - " + response);
                console.log("response - " + response.data);
                console.log("response title - " + response.data[1].title);
                // console.log("response author - " 
                //             + response.data[1].author);
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
                Dokumentu {this.state.nameOfWindow}
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

export default SubmitedDashBoard;