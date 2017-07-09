import React from 'react';

class Pagination extends React.Component {

    load(page) {
        this.props.pageRequested(page);
    }

    render() {  
        var pages = [];
        for(var i = 0; i < this.props.records.pages; i++) {
            var page = i + 1;
            var active = page === this.props.records.criteria.page ? "pagination-element pagination-active" : "pagination-element";
            pages.push({number:page, activeClass:active});
        }
        
        return (
            <div>
                {this.props.records.pages !== 1 &&
                    <div className="pagination-container">
                        {pages.map((page) => 
                            <div key={page.number} className={page.activeClass} onClick={ () => this.load(page.number) }>{page.number}</div>
                        )}
                    </div>
                }
            </div>
        );
    }
    
}

export default Pagination;