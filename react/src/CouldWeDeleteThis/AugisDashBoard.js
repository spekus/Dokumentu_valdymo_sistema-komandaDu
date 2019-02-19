// import React, {Component} from 'react';
// import DocumentsList from "../Documents/ReactFragments/AugustasDocumentsList";
// import axios from 'axios';


// class AugisDashBoard extends Component {
//     state = { 
//         userId : 'id123',
//         userDocuments : [],
//     }
//     componentWillMount(){
//         this.getAllDocuments();
//     }
//     doStuff2(event) {
//         console.log('this is identifier:' + event.name);
//     }
//     doStuff(parametras) {
//         console.log('this is :' + parametras);
//     }

//     getAllDocuments() {
//         console.log("runing getAllDocuments");
//         axios.get('/api/documents/' + this.state.userId + '/documents')
//             .then(response => {
//                 console.log("response - " + response);
//                 console.log("response - " + response.data);
//                 console.log("response title - " + response.data[1].title);
//                 console.log("response author - " 
//                             + response.data[1].author);
//                 // const dataList =
//                 // response.data.map((p) => {
//                 //     return (
//                 //         <div>
//                 //     <h3>titile {p.title}</h3>
//                 //     <h3>author {p.author}</h3>
//                 //         </div>
//                 //     );
//                 // });
//                 // this.setState({masyvas : dataList})
//                 // console.log("masyvas - " + dataList[0].author);
//                 this.setState({userDocuments : response.data})
//             })
//             .catch(err => {
//                 this.setState({error: err.message})
//                 console.log("Error from /api/documents/{userIdentifier}/documents - " 
//                 + err)
//             });
//     }


//     render() {
//         return (
//             <React.Fragment>
//                 <div className="row mt-2">
//                     <div className='col-lg-12'>
//                         {/* <h5>Atmesti</h5> */}
//                         <DocumentsList list={this.state.userDocuments}
//                         press={this.doStuff}
//                         press2={this.doStuff2}/>
//                     </div>
//                 </div>
//             </React.Fragment>
//         );
//     }
// }

// export default AugisDashBoard;