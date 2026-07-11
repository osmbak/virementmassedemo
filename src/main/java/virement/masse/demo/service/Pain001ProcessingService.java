
package virement.masse.demo.service;

import virement.masse.demo.dto.Pain001ProcessResponse;

import java.io.InputStream;

public interface Pain001ProcessingService {

	Pain001ProcessResponse processPain001(InputStream inputStream, String fileName);
}