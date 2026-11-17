import java.io.*;
import java.net.*;

/**
 * This class loads a UK DEFRA air pollution file from disk and returns the file data
 * in an object. 
 * 
 * DEFRA files are CSV files in a specific format. See https://uk-air.defra.gov.uk/data/pcm-data
 * for detailed information.
 *
 * @author Michael Kölling
 * @version 1.0
 */
public class DataLoader
{
    private static final String COMMA_DELIMITER = ",";

    /** 
     * Read a data file from disk. The data must be a CSV file, and must be in the
     * DEFRA air pollution file format. The data is returned in a DataSet object.
     * 
     * @return A DataSet object holding the complete dataset
     */
    public DataSet loadDataFile(String fileName) 
    {
        System.out.println("Loading file " + fileName + "...");

        File file = new File(fileName);  // Use File object directly
        if (!file.exists()) {
            System.out.println("File does not exist: " + fileName);
            return null;
        }

        URL url = getClass().getResource(fileName);
        try (BufferedReader br = new BufferedReader(new FileReader(new File(url.toURI()).getAbsolutePath()))) {
            // the first four lines of the file hold special information; read them in:
            String pollutant = readDataHeader(br);
            String year = readDataHeader(br);
            String metric = readDataHeader(br);
            String units = readDataHeader(br);

            // the column labels for the data points.
            br.readLine();
            br.readLine();

            DataSet dataSet = new DataSet(pollutant, year, metric, units);

            // read all the data lines
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                dataSet.addData(values);
            }
            System.out.println("Loading file... done.");
            return dataSet;
        }        
        catch(IOException | URISyntaxException e) {
            System.out.println("Could not read file " + fileName);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Read one piece of information out of the header of the data file.
     * Each line in the header has the format
     *    DATA,,,
     * That is, it is a four-column CSV entry where the first column holds the data we
     * want and the other three columns are empty.
     * We read and return only the data point from the first column.
     * 
     * @return The data from the next header line of the file currently being read.
     */
    private String readDataHeader(BufferedReader br) throws IOException
    {
        String line = br.readLine();
        String[] values = line.split(COMMA_DELIMITER);
        return values[0];
    }

    /**
     * Display all files in the specified directories.
     */
    public void showFiles() {
        DataLoader loader = new DataLoader();

        // List of directories to check for pollution data files
        String[] directories = {
                "UKAirPollutionData/NO2",
                "UKAirPollutionData/PM10",
                "UKAirPollutionData/PM25"
            };

        for (String dirPath : directories) {
            File directory = new File(dirPath);

            // List all CSV files in the current directory
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".csv"));

            if (files != null && files.length > 0) {
                for (File file : files) {
                    // Check the file type by its

                    // Check the file type by its name and load the relevant dataset
                    if (file.getName().contains("no2")) {
                        //System.out.println("Loading NO2 file: " + file.getName());
                        DataSet dataSet = loader.loadDataFile(file.getPath());
                        processData("NO2", dataSet);
                    } 
                    else if (file.getName().contains("pm10")) {
                        //System.out.println("Loading PM10 file: " + file.getName());
                        DataSet dataSet = loader.loadDataFile(file.getPath());
                        processData("PM10", dataSet);
                    } 
                    else if (file.getName().contains("pm25")) {
                        //System.out.println("Loading PM2.5 file: " + file.getName());
                        DataSet dataSet = loader.loadDataFile(file.getPath());
                        processData("PM25", dataSet);

                    } else {
                        System.out.println("No CSV files found in the directory: " + dirPath);
                    }
                }
            }
        }
    }

    // Process the data for a specific type of pollutant
    private void processData(String pollutantType, DataSet dataSet) {
        if (dataSet != null) {
            System.out.println("Data loaded successfully for " + pollutantType);
            // Print or process the dataset here (e.g., print the data)
            System.out.println(dataSet);
        } else {
            System.out.println("Failed to load data for " + pollutantType);
        }
    }
}