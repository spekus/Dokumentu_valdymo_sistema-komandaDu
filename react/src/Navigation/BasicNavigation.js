import React from "react";
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import AddFile from '../Files/Files.js';

function BasicNavigation() {
  return (
    <Router>
      <div>
        <ul>
          <li>
            <Link to="/">1</Link>
          </li>
          <li>
            <Link to="/formsvente">2</Link>
          </li>
          <li>
            <Link to="/formvalstybe">3</Link>
          </li>
          <li>
            <Link to="/addfile">Prideti Faila</Link>
          </li>
        </ul>

        <hr />

        <Route exact path="/" component={DisplayHome} />
        {/* <Route path="/1" component={FormRun2} />
        <Route path="/2" component={FormRun} /> */}
        <Route path="/addfile" component={DisplayFileUploading} />
        {/* <Route path="/3" component={SventeDetailed} />  */}
      </div>
    </Router>
  );
}

function DisplayFileUploading() {
  return (
    <div>
      <AddFile/>
    </div>
  );
}

function FormRun() {
  return (
    <div>
      {/* <ValstybesForm/> */}
    </div>
  );
}


function FormRun2(){
  return (
    <div>
      {/* <SventesForma/> */}
    </div>
  );
}
function DisplayHome(){
    return (
        <div>
          <div>
            {/* <Sventes/> */}
          </div>
        </div>
    );
}




export default BasicNavigation;
