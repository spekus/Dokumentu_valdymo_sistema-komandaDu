import React, {Component} from 'react';
import axios from "axios/index";
import DateRangePicker from '@wojtekmaj/react-daterange-picker';
import Chart from "react-google-charts";


class Charts extends Component {

    state = {
        date:[new Date().toISOString(),new Date().toISOString()],
        approvedStatistics: [],              //Patvirtintų dokumentų statistika per nustatytą laikotarpį
        rejectedStatistics: [],              //Atmestų dokumentų statistika per nustatytą laikotarpį
        postedStatistics: [],                //Pateiktų dokumentų statistika per nustatytą laikotarpį
        userListByPostedDocs:[],            //Vartotojų sąrašas, surikiuotas pagal daugiausiai pateiktų dokumentų skaičių
            mappedApproved:[],                   //sumapintas approvedStatistics
            mappedRejected:[],                   //sumapintas rejectedStatistics
            mappedPosted:[],                   //sumapintas postedStatistics
            mappedUserList:[]                  //sumapintas userList
    }


    //########## kalendorius ##########

    onChange = date => {
        if(date!==null){
            this.setState({ date })
        }
        else { this.setState({date:''})}
    }

    //########## mygtuko paspaudimas "Gauti statistiką" iškviečia AXIOS duomenų gavimui iš back'endo ##########

    getStatistics=()=>{
        this.getApprovedData();
        this.getRejectedData();
        this.getPostedData();
        this.getUserListByPostedDocs();
    }

    //########## Metodai, ateinančių duomenų iš BACKENDO apdorojimui ir sukišimui į statistikos masyvą##########

    // componentDidMount=()=>{
    //
    // }



    approvedData=()=>{
        this.state.mappedApproved=[["Element", "Skaičius", { role: "style" }]];
            this.state.approvedStatistics.map(item => {
                let obj = [item.type, parseInt(item.count), "#" + ("000000" + Math.floor(Math.random() * 16777216).toString(16)).substr(-6)];
                this.state.mappedApproved.push(obj)
                console.log("pushed this:" + obj);
            });
    }

    rejectedData=()=>{
        this.state.mappedRejected=[["Element", "Skaičius", { role: "style" }]];
        this.state.rejectedStatistics.map(item => {
            let obj = [item.type, parseInt(item.count), "#" + ("000000" + Math.floor(Math.random() * 16777216).toString(16)).substr(-6)];
            this.state.mappedRejected.push(obj)
            console.log("pushed this:" + obj);
        });
    }

    postedData=()=>{
        this.state.mappedPosted=[["Element", "Skaičius", { role: "style" }]];
        this.state.postedStatistics.map(item => {
            let obj = [item.type, parseInt(item.count), "#" + ("000000" + Math.floor(Math.random() * 16777216).toString(16)).substr(-6)];
            this.state.mappedPosted.push(obj)
            console.log("pushed this:" + obj);
        });
    }

    userListData=()=>{
        this.state.mappedUserList=[["Element", "Skaičius", { role: "style" }]];
        this.state.userListByPostedDocs.map(item => {
                    let obj = [item.type, parseInt(item.count), "#" + ("000000" + Math.floor(Math.random() * 16777216).toString(16)).substr(-6)];
                    this.state.mappedUserList.push(obj)
                    console.log("pushed this:" + obj);
        });
    }



    //########## Duomenų gavimas iš BACKENDO ##########

    getApprovedData=()=>{
        axios({
            method: 'get',
            url: '/api/statistics/approved-docs',
            params: {
                startDate: this.state.date[0],
                endDate: this.state.date[1]
            },
            headers: {'Content-Type': 'application/json;charset=utf-8'}
        })
            .then(response => {
                this.setState({approvedStatistics: response.data});
                console.log(response.data)
            })
            .then(this.approvedData)
            .catch(error => {
                console.log("Klaida iš statistikos kontrollerio APPROVED-DOCS" + error.message)
            })
    }

    getRejectedData=()=>{
        axios({
            method: 'get',
            url: '/api/statistics/rejected-docs',
            params: {
                startDate: this.state.date[0],
                endDate: this.state.date[1]
            },
            headers: {'Content-Type': 'application/json;charset=utf-8'}
        })
            .then(response => {
                this.setState({rejectedStatistics: response.data});
                console.log(response.data)
            })
            .then(this.rejectedData)
            .catch(error => {
                console.log("Klaida iš statistikos kontrollerio REJECTED-DOCS" + error.message)
            })
    }

    getPostedData=()=>{
        axios({
            method: 'get',
            url: '/api/statistics/posted-docs',
            params: {
                startDate: this.state.date[0],
                endDate: this.state.date[1]
            },
            headers: {'Content-Type': 'application/json;charset=utf-8'}
        })
            .then(response => {
                this.setState({postedStatistics: response.data});
                console.log(response.data)
            })
            .then(this.postedData)
            .catch(error => {
                console.log("Klaida iš statistikos kontrollerio POSTED-DOCS" + error.message)
            })
    }

    getUserListByPostedDocs=()=>{
        axios({
            method: 'get',
            url: '/api/statistics/userlist-by-posted-docs',
            headers: {'Content-Type': 'application/json;charset=utf-8'}
        })
            .then(response => {
                this.setState({userListByPostedDocs: response.data});
                console.log(response.data)
            })
            .then(this.userListData)
            .catch(error => {
                console.log("Klaida iš statistikos kontrollerio USERLIST-BY-POSTED-DOCS" + error.message)
            })
    }

    render(){
        let chartApproved='';
        let chartRejected='';
        let chartPosted='';
        let chartUserList='';
           if(this.state.mappedApproved[1]&&this.state.mappedRejected[1]&&this.state.mappedPosted[1]){
               chartApproved=<Chart
                   chartType="ColumnChart"
                   color="red"
                   width="100%"
                   height="240px"
                   data={this.state.mappedApproved}
               />
               chartRejected=<Chart
                   chartType="ColumnChart"
                   color="red"
                   width="100%"
                   height="240px"
                   data={this.state.mappedRejected}
               />
               chartPosted=<Chart
                   chartType="ColumnChart"
                   color="red"
                   width="100%"
                   height="240px"
                   data={this.state.mappedPosted}
               />
               chartUserList=<Chart
                   chartType="ColumnChart"
                   color="red"
                   width="100%"
                   height="240px"
                   data={this.state.mappedUserList}
               />
           }

        return(
            <div>
                <div className='shadow p-3 mb-5 bg-white rounded'>
                    <h5>Dokumentų tipų statistika</h5>
                    <div className="row">
                        <div className="col-md-8">
                                <DateRangePicker onChange={this.onChange} value={this.state.date}/>
                                <button onClick={this.getStatistics}>Išvesti statistiką</button>
                        </div>
                    </div>

                 </div>
                <div className='shadow p-3 mb-5 bg-white rounded'>
                    <div className="row">
                            <div className="col-md-6">
                                <br />
                                <h5>Patvirtintų dokumentų statistika</h5>
                                {chartApproved}
                            </div>
                    </div>
                </div>
                <div className='shadow p-3 mb-5 bg-white rounded'>
                    <div className="row">
                        <div className="col-md-6">
                            <br />
                            <h5>Atmestų dokumentų statistika</h5>
                            {chartRejected}
                        </div>
                    </div>
                </div>
                <div className='shadow p-3 mb-5 bg-white rounded'>
                    <div className="row">
                        <div className="col-md-6">
                            <br />
                            <h5>Pateiktų dokumentų statistika</h5>
                            {chartPosted}
                        </div>
                    </div>
                </div>
                <div className='shadow p-3 mb-5 bg-white rounded'>
                    <div className="row">
                        <div className="col-md-6">
                            <br />
                            <h5>Naudotojų, dažniausiai pateikiančių dokumentų statistika</h5>
                            {chartUserList}
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default Charts;


