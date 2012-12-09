package edu.skynet.afib.parsers;

import java.io.FileNotFoundException;

import edu.skynet.dataimport.parsers.AnnotationParser;

public class AfibAnnotationParser extends AnnotationParser {

	public AfibAnnotationParser(String annotations) throws FileNotFoundException {
		super(annotations, "\n", "\\s+", 6, 1);
	}
}
