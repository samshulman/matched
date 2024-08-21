import React, { useState, useEffect , createContext, useContext, Component, SetStateAction } from "react";
import {Link } from 'react-router-dom'; //Redirect
import Main from './Main';

import { GoogleLogin, GoogleLogout} from 'react-google-login';
import {gapi} from 'gapi-script';

/**
 * This class handles the google authentication login and logout functionality.
 * The logged in and logged out states are toggled and the respective buttons
 * are displayed on the screen. Ensures only brown users can log in.
 */

let mainuseremail = ""
export {mainuseremail}

export default function Login (){
    const clientId = "952054202634-sh0ga8ll4khean9j7gfvcmqkdak5ssaq.apps.googleusercontent.com"
    const [profile, setProfile] = useState(null);
    const [emailID, setEmailID] = useState(null);

    
      useEffect(() => {
          const initClient = () => {
                  gapi.auth2.init({
                  clientId: clientId,
                  scope: ''
              });
          }
          gapi.load('client:auth2', initClient);
      })
    
      const onSuccess = (res: any) => {
        console.log(res.profileObj.email)
        if (res.profileObj.email.endsWith('@brown.edu')){
            setProfile(res.profileObj)
            setEmailID(res.profileObj.email)
            mainuseremail = res.profileObj.email
       }
      }

    
      const onFailure = (err: any) => {
          console.log('failed', err);
      }
    
      const logOut = () => {
          setProfile(null);
          setEmailID(null)
        
      }
    
      return (
          <div >
             {profile ? (
                        <div className="navbar-container">
                              <ul >
                                    <ol >
                                        <Link className="nav-link" to='/' >HOME</Link>
                                        <Link className="nav-link" to='/MatchOptions'  >NEW MATCHES </Link>
                                        <Link className="nav-link" to='/Profile' >MY MATCHES</Link> 
                                        
                                        <p id="emailID" >{emailID}</p>

                                        <GoogleLogout 
                                            clientId={clientId} 
                                            buttonText = "Log Out" 
                                            onLogoutSuccess={logOut}
                                            className="logout"
                                            aria-label="logout button"
                                        />
                                          
                                    </ol>
                                    </ul>
                            <Main/> 
                            
                             
                        </div>
                        
                   ) :(
                
                    <GoogleLogin    
                        clientId = {clientId}
                        buttonText = "Sign in with Google"
                        onSuccess={onSuccess}
                        onFailure={onFailure}
                        cookiePolicy={'single_host_origin'}
                        isSignedIn={true}
                        style={{ width:50, height:50, margin: 10 }}
                        aria-label="login button"
                        
                        
                        />
                        
                        )   }
    
          </div>

          )
    

}
