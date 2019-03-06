import React, {Component} from 'react';
import axios from "axios/index";
import DateRangePicker from '@wojtekmaj/react-daterange-picker';
import { Polar } from "react-chartjs-2";
import { MDBContainer } from "mdbreact";
import palette from "google-palette";


class Charts extends Component {

    state = {
        date: [new Date().toISOString(), new Date().toISOString()],
        approvedStatistics: [],              //Patvirtintų dokumentų statistika per nustatytą laikotarpį
        rejectedStatistics: [],              //Atmestų dokumentų statistika per nustatytą laikotarpį
        postedStatistics: [],                //Pateiktų dokumentų statistika per nustatytą laikotarpį
        userListByPostedDocs: [],            //Vartotojų sąrašas, surikiuotas pagal daugiausiai pateiktų dokumentų skaičių
            dataPolarApproved:{},           //Grafikų duomenų masyvai
            dataPolarRejected:{},
            dataPolarPosted:{},
            dataPolarUserlist:{},
        dropDownValue:1
    }


    handleChangeSelect = (event) => {
        this.setState({dropDownValue:parseInt(event.target.value)})
        console.log("eventas "+event.target.value)
        console.log("handle change "+this.state.dropDownValue)

    }

//########## kalendorius ##########
    onChange = date => {
        if(date!==null){
            this.setState({ date })
        }
        else { this.setState({date:''})}
    }

//########## mygtuko paspaudimas "Rodyti statistiką" iškviečia AXIOS duomenų gavimui iš back'endo ##########
    getStatistics=()=>{
        console.log("metode "+this.state.dropDownValue)
         if(this.state.dropDownValue===1){
             console.log("patvirtinti")
             this.getApprovedData();
         }

        if(this.state.dropDownValue===2){
             console.log("atmesti")
            this.getRejectedData();
        }

        if(this.state.dropDownValue===3){
            console.log("postinti")
            this.getPostedData();
        }

        if(this.state.dropDownValue===4){
            console.log("useriai")
            this.getUserListByPostedDocs();
        }
     }

//########## Graikų duomenų masyvai, kurie užpildomi gaunant data iš BACKendo ##########
    dataApproved=[];
    dataRejected=[];
    dataPosted=[];
    dataUserList=[];
    backgroundColorApproved=[];
    backgroundColorRejected=[];
    backgroundColorPosted=[];
    backgroundColorUserList=[];
    labelsApproved=[];
    labelsRejected=[];
    labelsPosted=[];
    labelsUserList=[];

//########## Iškviečiant mygtuku statistiką prasivalo masyvai, kad nekaupti duomenų jose ##########
    resetState=()=>{
        this.dataApproved=[];
        this.dataRejected=[];
        this.dataPosted=[];
        this.dataUserList=[];
        this.backgroundColorApproved=[];
        this.backgroundColorRejected=[];
        this.backgroundColorPosted=[];
        this.backgroundColorUserList=[];
        this.labelsApproved=[];
        this.labelsRejected=[];
        this.labelsPosted=[];
        this.labelsUserList=[];
    }

//########## Metodai, ateinančių duomenų iš BACKENDO apdorojimui ir sukišimui į statistikos masyvus ##########
    approvedData=()=>{  
        // choose colors here http://google.github.io/palette.js/
        var seq = palette('tol-dv', 5);
        var counter =0;
        this.resetState();
        this.state.approvedStatistics.forEach(item => {
            this.dataApproved.push(parseInt(item.count));
            this.backgroundColorApproved.push("#" + seq[counter]);
             counter = counter + 1;
            this.labelsApproved.push(item.type)
        });
        let mydata={
            datasets:[
                {
                    data:this.dataApproved,
                    backgroundColor:this.backgroundColorApproved,
                    label: "My dataset"
                }
            ],
            labels:this.labelsApproved
        }

        this.setState({dataPolarApproved:mydata})
        this.setState({state:this.state.dataPolarApproved});
    }

    rejectedData=()=>{
        var seq = palette('cb-GnBu', 5);
        var counter =0;
        this.resetState();
        this.state.rejectedStatistics.forEach(item => {
            this.dataRejected.push(parseInt(item.count));
 
            // this.backgroundColorRejected.push("#" + ("000000" + Math.floor(Math.random() * 16777216).toString(16)).substr(-6));
            this.backgroundColorRejected.push("#" + seq[counter]);
            counter = counter + 1;
            this.labelsRejected.push(item.type)
        });
        let mydata={
            datasets:[
                {
                    data:this.dataRejected,
                    backgroundColor:this.backgroundColorRejected,
                    label: "My dataset"
                }
            ],
            labels:this.labelsRejected
        }

        this.setState({dataPolarRejected:mydata})
        this.setState({state:this.state.dataPolarRejected});
    }

    postedData=()=>{
        var seq2 = palette('tol-dv', 10);
        var counter = 0;
        this.resetState();
        this.state.postedStatistics.forEach(item => {
            this.dataPosted.push(parseInt(item.count));

            // this.backgroundColorPosted.push("#" + ("000000" + Math.floor(Math.random() * 16777216).toString(16)).substr(-6));
            this.backgroundColorPosted.push("#" + seq2[counter]);
            counter = counter + 1;
            this.labelsPosted.push(item.type)
        });
        let mydata={
            datasets:[
                {
                    data:this.dataPosted,
                    backgroundColor:this.backgroundColorPosted,
                    label: "My dataset"
                }
            ],
            labels:this.labelsPosted
        }

        this.setState({dataPolarPosted:mydata})
        this.setState({state:this.state.dataPolarPosted});
    }

    userListData=()=>{
        var seq = palette('cb-GnBu', 7);
        var counter =0;
        this.resetState();
        this.state.userListByPostedDocs.forEach(item => {
            this.dataUserList.push(parseInt(item.count));
            // this.backgroundColorUserList.push("#" + ("000000" + Math.floor(Math.random() * 16777216).toString(16)).substr(-6));
            this.backgroundColorUserList.push("#" + seq[counter]);
            counter = counter + 1;
            this.labelsUserList.push(item.type)
        });
       let mydata={
            datasets:[
                {
                    data:this.dataUserList,
                    backgroundColor:this.backgroundColorUserList,
                    label: "My dataset"
                }
            ],
                labels:this.labelsUserList
        }

        this.setState({dataPolarUserlist:mydata})
        this.setState({state:this.state.dataPolarUserlist});
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
                console.log("response Approved come"+response.data)
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
                console.log("response Rejected come"+response.data)
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
                console.log("response Posted come"+response.data)
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
        console.log("renderio "+this.state.dropDownValue)
        if(this.state.dropDownValue===1) {
            var chartApproved = <MDBContainer>
                <h5>Patvirtintų dokumentų sąrašas</h5>
                <Polar data={this.state.dataPolarApproved} options={{responsive: true}}/>
            </MDBContainer>
        }

        if(this.state.dropDownValue===2) {
            var chartRejected = <MDBContainer>
                <h5>Atmestų dokumentų sąrašas</h5>
                <Polar data={this.state.dataPolarRejected} options={{responsive: true}}/>
            </MDBContainer>
        }

        if(this.state.dropDownValue===3){
        var chartPosted=<MDBContainer>
            <h5>Pateiktų dokumentų sąrašas</h5>
            <Polar data={this.state.dataPolarPosted} options={{ responsive: true }} />
        </MDBContainer>
        }

         if(this.state.dropDownValue===4) {
            var userList = <MDBContainer>
                <h5>Dažniausiai pateikiančių dokumentų vartotojų sąrašas</h5>
                <Polar data={this.state.dataPolarUserlist} options={{responsive: true}}/>
            </MDBContainer>
        }

        return(
            <div className="container">
                <h5>Laiko intervalas</h5>
                <div className='shadow p-3 mb-5 bg-white rounded'>
                    <div className="row">
                        <div className="col-md-4">
                            <DateRangePicker onChange={this.onChange} value={this.state.date}/>
                        </div>

                        <div className="col-md-4">
                            <select className="form-control" value={this.state.dropDownValue} onChange={this.handleChangeSelect} >
                                <option value="1">Patvirtinti dokumentai</option>
                                <option value="2">Atmesti dokumentai</option>
                                <option value="3">Pateikti dokumentai</option>
                                <option value="4">Vartotojų sąrašas</option>
                            </select>
                        </div>
                        <div className="col-md-4">
                            <button className="btn button1" onClick={this.getStatistics}>Rodyti</button>
                        </div>
                    </div>
                </div>

                <div className='shadow p-3 mb-5 bg-white rounded'>
                    <div className="row">
                        <div className="col-md-12">
                            {chartApproved}
                            {chartRejected}
                            {chartPosted}
                            {userList}
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default Charts;


