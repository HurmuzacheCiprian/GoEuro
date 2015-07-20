package go.euro.utils;

import go.euro.enums.FileFormat;
import go.euro.model.CsvModel;
import go.euro.model.GoEuroModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;

/**
 * Created by churmuzache on 7/20/15.
 */
public abstract class GoEuroFileWriter {

    private static final Logger logger = LoggerFactory.getLogger(GoEuroFileWriter.class);

    public static <T> void writeToFile(String fileName,FileFormat fileFormat, T[] entity) {
        switch(fileFormat) {
            case CSV:
                if(entity instanceof GoEuroModel[]) {
                    GoEuroModel[] model = (GoEuroModel[])entity;


                    final String fileHeader = "_id,name,type,latitude,longitude";
                    final String delimiter = ",";
                    final String newLine = "\n";

                    FileWriter fileWriter = null;

                    try {
                        fileWriter = new FileWriter(fileName);
                        fileWriter.append(fileHeader);
                        fileWriter.append(newLine);

                        for(int i=0;i<model.length;i++) {
                            CsvModel csvModel = new CsvModel();

                            csvModel.set_id(model[i].get_id());
                            csvModel.setName(model[i].getName());
                            csvModel.setType(model[i].getType());
                            csvModel.setLatitude(model[i].getGeo_position().getLatitude());
                            csvModel.setLongitude(model[i].getGeo_position().getLongitude());

                            fileWriter.append(String.valueOf(csvModel.get_id()));
                            fileWriter.append(delimiter);
                            fileWriter.append(csvModel.getName());
                            fileWriter.append(delimiter);
                            fileWriter.append(csvModel.getType());
                            fileWriter.append(delimiter);
                            fileWriter.append(String.valueOf(csvModel.getLatitude()));
                            fileWriter.append(delimiter);
                            fileWriter.append(String.valueOf(csvModel.getLongitude()));
                            fileWriter.append(delimiter);

                        }

                    } catch(Exception exception) {
                        logger.error("Error while writing to file...");
                    } finally {
                        try {
                            fileWriter.flush();
                            fileWriter.close();
                        } catch(Exception exception) {
                            logger.error("Error with file...");
                        }
                    }
                }

                break;
            case TXT:
                //TODO for next implementations
                break;
            default:

                break;
        }
    }



}
