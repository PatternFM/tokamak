import React, { Component } from 'react';

import Header from '../components/Header.jsx'
import Footer from '../components/Footer.jsx'

class Layout extends Component {
  render() {
    return (
      <div id="container">
        <Header />
        <div id="content">
          {this.props.children}
        </div>
        <Footer />
      </div>
    );
  }
}

export default Layout;
