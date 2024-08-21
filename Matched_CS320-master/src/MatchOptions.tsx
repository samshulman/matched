import React from 'react';
import Button from '@mui/material/Button';
import './App.css';
import { Link} from 'react-router-dom';
// import pencil from './images/friend.png'
// import friend from './images/friend.png'
// import heart from './images/heart.png'

/**
 * This class displays all of the match options
 * by returning the three buttons that respectively
 * link to the different match option questionnaires
 * @returns 
 */

function MatchOptions (){
    return (

        <div>
            <div id="center-text">
                <p>Match options below: </p>
            </div>
            
            <div id="button-row">

                <Link id="circ-item" to='/QuestionnaireStudy'>
                <div >
                    <p id ="button-style" aria-label="study match option">STUDY</p>
                    <div>
                        {/* <img id="icon-img" src={pencil} alt="pencil"/> */}
                    </div>
                </div>
                </Link>

                <Link id="circ-item" to='/QuestionnaireFriend'>
                <div >
                    <p id="button-style" aria-label="friend match option">FRIEND</p>
                    <div>
                        {/* <img id="icon-img" src={friend} alt="friend"/> */}
                    </div>
                </div>
                </Link>

                <Link id="circ-item" to='/QuestionnaireDate'>
                <div >
                    <p id="button-style" aria-label="date match option">DATE</p>
                    <div>
                        {/* <img id="icon-img" src={heart}  alt="heart"/> */}
                    </div>
                </div>
                </Link>

            </div>

        </div>
        
        ) 

}

export default MatchOptions;