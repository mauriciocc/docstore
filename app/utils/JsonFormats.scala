package utils

import java.sql.Timestamp
import java.util.Date

import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json.{JsString, JsSuccess, JsResult, JsValue}

object JsonFormats {

/*  implicit val dateFormat: play.api.libs.json.Format[Date] = new play.api.libs.json.Format[Date] {

    val dateTimeFormat = ISODateTimeFormat.dateTime

    override def reads(json: JsValue): JsResult[Date] = {
      JsSuccess(new Date(dateTimeFormat.parseDateTime(json.as[String]).getMillis))
    }

    override def writes(o: Date): JsValue = {
      JsString(dateTimeFormat.print(o.getTime))
    }

  }*/

  implicit val timestampFormat: play.api.libs.json.Format[Timestamp] = new play.api.libs.json.Format[Timestamp] {

    val dateTimeFormat = ISODateTimeFormat.dateTime

    override def reads(json: JsValue): JsResult[Timestamp] = {
      JsSuccess(new Timestamp(dateTimeFormat.parseDateTime(json.as[String]).getMillis))
    }

    override def writes(o: Timestamp): JsValue = {
      JsString(dateTimeFormat.print(o.getTime))
    }

  }
}
