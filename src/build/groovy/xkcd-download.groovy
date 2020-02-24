import org.apache.commons.io.*
import org.jsoup.*
import org.jsoup.nodes.*
import org.jsoup.select.*
import com.overzealous.remark.Remark;

log.info "Starting crawling of xkcd strips"

var cache = new File(project.properties["cache"], "pages")
cache.mkdirs()
var url = project.properties["explain"]
var images = new File(project.properties["images"])
var transcripts = new File(project.properties["transcripts"])
log.info "Searching for all strips on $url"
Remark remark = new Remark();

var index = 1
var found = false
do {
	var currentUrl = url+index
	var htmlFile = new File(cache, index+".html")
	if(!htmlFile.exists()) {
		log.info("page of $index doesn't exists locally. Downloading")
		FileUtils.copyURLToFile(new URL(currentUrl), htmlFile)
	}
	// Now page exists locally, parse it using Jsoup
	Document content = Jsoup.parse(htmlFile, "UTF-8", currentUrl)
	// Expose page title as a test
	log.info "Parsing content from "+content.getElementById("firstHeading").text()
	// Now get images contained in a <a class="image" ...> tag
	Elements imageContainers = content.select("a.image img")
	URL imageUrl = new URL(imageContainers[0].attr("abs:src"))
	log.debug "Image is at "+imageUrl
	String imageExtension = FilenameUtils.getExtension(imageUrl.getPath())
	if(imageExtension in ["jpeg", "jpg"]) {
		File image = new File(images, index+".jpg")
		if (!image.exists()) {
			FileUtils.copyURLToFile(imageUrl, image)
		}
		File transcript = new File(transcripts, index+".txt")
		if(!transcript.exists()) {
			transcriptSpan = content.getElementById("Transcript")
			if(transcriptSpan!=null) {
				transcriptHeader = transcriptSpan.parent()
				transcriptLine = transcriptHeader.nextElementSibling()
				transcriptText = ""
				while(!(transcriptLine.normalName() in ['h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'h7'])) {
					log.debug "adding to transcript "+transcriptLine.text()
					transcriptText += transcriptLine.html() + "\n"
					transcriptLine = transcriptLine.nextElementSibling()
				}
				// Now make all that html nice pure text
				transcriptText = remark.convertFragment(transcriptText)
				FileUtils.write(transcript, transcriptText, "UTF-8")
			}
		}
	}
	index++
} while(true)
