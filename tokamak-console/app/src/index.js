import React from 'react';
import ReactDom from 'react-dom';
import { BrowserRouter as Router, Route } from 'react-router-dom'

import layout from './css/layout.css';
import login from './css/login.css';

import Home from './pages/Home.jsx';
import Login from './pages/Login.jsx';

ReactDom.render(
  <Router>
    <div>
      <Route exact path="/" component={Home}/>
      <Route exact path="/console" component={Home}/>
      <Route path="/login" component={Login}/>
    </div>
  </Router>

, document.getElementById("tokamak-console"));