package uk.co.pollett.scala

import java.net.URL

import com.gargoylesoftware.htmlunit.html.{DomAttr, DomNode, HtmlPage, HtmlElement}
import com.gargoylesoftware.htmlunit.{SgmlPage, BrowserVersion, WebClient}

import scala.collection.JavaConversions._

object Main {

  implicit class HtmlElementCompare(e: HtmlElement){
    def depth: Int = {
      var dom: DomNode = e.getParentNode
      var i = 0
      while(dom != null){
        i=i+1
        dom = dom.getParentNode
      }
      i
    }
  }

  def main(args: Array[String]) {

    val url = new URL("http://www.asos.com/Men/A-To-Z-Of-Brands/Asos-Collection/Cat/pgecategory.aspx?cid=3159")
    val price_reg = "£[0-9]{1,}".r

    val webClient = new WebClient(BrowserVersion.CHROME)
    webClient.getOptions.setCssEnabled(false)
    webClient.getOptions.setDoNotTrackEnabled(true)
    webClient.getOptions.setThrowExceptionOnFailingStatusCode(false)
    webClient.getOptions.setThrowExceptionOnScriptError(false)
    val page = webClient.getPage(url).asInstanceOf[HtmlPage]

    println(page.getTitleText)

    val elements = page.getByXPath("//*")
    for (element <- elements) {
      val el = element.asInstanceOf[HtmlElement]
      val content = el.getTextContent
      if (price_reg.findAllIn(content).nonEmpty) {
        val price = price_reg.findFirstIn(el.getTextContent).get
        if(!price.equals("£0")) {
          println("distance: " + el.depth)
          println(el.getAttribute("class"))
          println(price)
          println("--")
        }
      }
    }
  }
}