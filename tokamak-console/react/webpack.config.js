var webpack = require("webpack");
var path = require("path");
var html = require('html-webpack-plugin');

var BUILD_DIR = path.resolve(__dirname, "../src/main/resources/public");
var APP_DIR = path.resolve(__dirname, "app");

var config = {
  entry: APP_DIR + "/index.js",
    
  output: {
    path: BUILD_DIR,
    filename: "bundle.js"
  },
  
  plugins: [new html({
	  hash: true,
	  template: "./index.html"
  })],
  
  module: {
    loaders: [
	  {
	    test: /\.jsx?$/,
	    exclude: [/node_modules/],
	    loaders: ["babel-loader"]
	  }
	]
  }  

};

module.exports = config;