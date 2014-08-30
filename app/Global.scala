import play.api._
import play.api.mvc._
import play.filters.gzip.GzipFilter
import play.filters.headers.SecurityHeadersFilter

object Global extends WithFilters(new GzipFilter()/*, SecurityHeadersFilter()*/) with GlobalSettings {
 
}