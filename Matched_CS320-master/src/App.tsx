import { NONAME } from 'dns';
import * as React from 'react';
import { Link} from 'react-router-dom';
import Main from './Main';
import LogoutIcon from '@mui/icons-material/Logout';
import { IconButton } from '@mui/material';
import './App.css';
import Login from './Login'

/**
 * This class sets up the react app for the map.
 * @returns the google auth login
 */

function App(){
  return (

   <Login/>
  );
}

export default App;
