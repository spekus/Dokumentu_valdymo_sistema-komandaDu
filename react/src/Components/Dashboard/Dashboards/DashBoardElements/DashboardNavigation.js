import React from 'react';
import {Link} from 'react-router-dom';


const DashboardNavigation = (props) => {

    return (
        <React.Fragment>
                   <div className='col-lg-2'> 
                        <Link className="btn btn-dark btn-lg btn-block" to=
                        {"/"}>
                        Visi dokumentai 
                        </Link>
                    </div>
                    <div className='col-lg-2'> 
                        <Link className="btn btn-dark btn-lg btn-block" to=
                        {"/dashboard/documents/created/"}>
                        {/* Submitted documents */}
                        Sukurti dokumentai
                        </Link>
                    </div>
                    <div className='col-lg-2'> 
                        <Link className="btn btn-dark btn-lg btn-block" to=
                        {"/dashboard/documents/submitted/"}>
                        {/* Submitted documents */}
                        Pateikti dokumentai
                        </Link>
                    </div>
                    <div className='col-lg-2'> 
                        <Link className="btn btn-dark btn-lg btn-block" to=
                        {"/dashboard/documents/approved/"}>
                        {/* Approved documents */}
                        Patvirtinti dokumentai
                        </Link>
                    </div>
                    <div className='col-lg-2'> 
                        <Link className="btn btn-dark btn-lg btn-block" to=
                        {"/dashboard/documents/rejected/"}>
                        {/* Rejected documents */}
                        Atmesti dokumentai
                        </Link>
                    </div>
                    <div className='col-lg-2'> 
                        <Link className="btn btn-dark btn-lg btn-block" to=
                        {"/dashboard/documents/to_aproove/"}>
                        {/* Rejected documents */}
                        Dokumentai tvirtinimui
                        </Link>
                    </div>

        </React.Fragment>
    );
};

export default DashboardNavigation;
