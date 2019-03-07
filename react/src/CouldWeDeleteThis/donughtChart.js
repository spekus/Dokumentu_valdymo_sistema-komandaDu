// import React, {Component} from 'react';
// import axios from "axios/index";
// import DateRangePicker from '@wojtekmaj/react-daterange-picker';
// import PieChart from 'react-minimal-pie-chart';


// class Charts extends Component {

//     state = {
//         date: [new Date().toISOString(), new Date().toISOString()],
//         approvedStatistics: [],
//         userListByPostedDocs:[],
//         mappedApproved: [],
//         mappedUserList:[]
//     }


//     //########## kalendorius ##########

//     onChange = date => {
//         if(date!==null){
//             this.setState({ date })
//         }
//         else { this.setState({date:''})}
//     }

//      getStatistics=()=>{
//         this.getApprovedData();
//         this.getUserListByPostedDocs();
//     }
    

//     approvedData=()=>{
//         this.state.mappedApproved=[]
//         this.state.approvedStatistics.map(item => {
//             let obj = {title:item.type, value:parseInt(item.count), color:"#" + ("000000" + Math.floor(Math.random() * 16777216).toString(16)).substr(-6)};
//             this.state.mappedApproved.push(obj)
//             console.log("pushed Approved this:" + obj);
//         });
//         this.setState({state:this.state.mappedApproved});
//     }


//     userListData=()=>{
//         this.state.mappedUserList=[]
//         this.state.userListByPostedDocs.map(item => {
//             let obj = {title:item.type, value:parseInt(item.count), color:"#" + ("000000" + Math.floor(Math.random() * 16777216).toString(16)).substr(-6)};
//             this.state.mappedUserList.push(obj)
//             console.log("pushed this:" + obj);
//         });
//         this.setState({state:this.state.mappedUserList});
//     }



//     getApprovedData=()=>{
//         var distinctColors = require('distinct-colors')
//         var palette = distinctColors() // You may pass an optional config object
//         axios({
//             method: 'get',
//             url: '/api/statistics/approved-docs',
//             params: {
//                 startDate: this.state.date[0],
//                 endDate: this.state.date[1]
//             },
//             headers: {'Content-Type': 'application/json;charset=utf-8'}
//         })
//             .then(response => {
//                 this.setState({approvedStatistics: response.data});
//                 console.log("response Approved come"+response.data)
//             })
//             .then(this.approvedData)
//             .catch(error => {
//                 console.log("Klaida iš statistikos kontrollerio APPROVED-DOCS" + error.message)
//             })
//     }

//     getUserListByPostedDocs=()=>{
//         axios({
//             method: 'get',
//             url: '/api/statistics/userlist-by-posted-docs',
//             headers: {'Content-Type': 'application/json;charset=utf-8'}
//         })
//             .then(response => {
//                 this.setState({userListByPostedDocs: response.data});
//                 console.log(response.data)
//             })
//             .then(this.userListData)
//             .catch(error => {
//                 console.log("Klaida iš statistikos kontrollerio USERLIST-BY-POSTED-DOCS" + error.message)
//             })
//     }

//     render(){
//         let chartApproved='';
//         let chartUserList='';
//         if(this.state.mappedUserList[0]&&this.state.mappedApproved[0]){
//             chartApproved=<PieChart
//                 data={this.state.mappedApproved}
//                 label={({ data, dataIndex }) =>
//                     data[dataIndex].title+" "+data[dataIndex].value
//                 }

//                 labelStyle={{
//                     fontSize: '2.5px',
//                     fontFamily: 'arial',
//                 }}
//                 radius={30}
//                 labelPosition={112}
//                 lineWidth={15}
//                 paddingAngle={5}
//                 lengthAngle={-360}
//                 lineWidth={25}
//             />
//             chartUserList=<PieChart
//                 data={this.state.mappedUserList}
//                 label={({ data, dataIndex }) =>
//                     data[dataIndex].title+" "+data[dataIndex].value
//                 }

//                 labelStyle={{
//                     fontSize: '2.5px',
//                     fontFamily: 'arial',
//                 }}
//                 radius={30}
//                 labelPosition={112}
//                 lineWidth={15}
//                 paddingAngle={5}
//                 lengthAngle={-360}
//                 lineWidth={25}
//                 />
//         }

//         return(

//             <div className="container">
//                 <h5>Laiko intervalas</h5>
//                 <div className='shadow p-3 mb-5 bg-white rounded'>
//                         <div className="row">
//                             <div className="col-md-4">
//                                 <DateRangePicker onChange={this.onChange} value={this.state.date}/>
//                             </div>
//                             <div className="col-md-4">
//                                 <button className="btn button1 btn-sm" onClick={this.getStatistics}>Rodyti statistiką</button>
//                             </div>
//                         </div>
//                 </div>

//                 <div className='shadow p-3 mb-5 bg-white rounded'>
//                     <div className="row">
//                         <div className="col-md-6">
//                             <h6>Approved List</h6>
//                             {chartUserList}
//                         </div>

//                         <div className="col-md-6">
//                             <h6>Rejected List</h6>
//                             {chartApproved}
//                         </div>
//                     </div>
//                 </div>
//             </div>
//         )
//     }
// }

// export default Charts;


