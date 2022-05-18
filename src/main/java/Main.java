import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class Bored extends Frame implements ActionListener {
    Label participants_lbl, activity_lbl, max_price_lbl, min_price_lbl;
    TextField participants_num, max_price_num, min_price_num;
    Button participants_btn, random_activity_btn, type_activity_btn, price_btn, add_to_file_btn, close_btn;
    Choice chooser = new Choice();
    public Bored()
    {
        participants_lbl = new Label("Participants");
        participants_num = new TextField(10);
        participants_btn = new Button("Get activity by participants number");

        min_price_num = new TextField(10);
        min_price_lbl = new Label("Min price");
        max_price_num = new TextField(10);
        max_price_lbl = new Label("Max price");
        price_btn = new Button("Get activity by price");

        activity_lbl = new Label("");

        type_activity_btn = new Button("Get activity by type");
        random_activity_btn = new Button("Get random activity");
        add_to_file_btn = new Button("Add to file");
        close_btn = new Button("Close");

        chooser.add("education");
        chooser.add("recreational");
        chooser.add("social");
        chooser.add("diy");
        chooser.add("charity");
        chooser.add("cooking");
        chooser.add("relaxation");
        chooser.add("music");
        chooser.add("busywork");

        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(new GridBagLayout());

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(chooser, gbc);
        gbc.gridx = 1;
        add(type_activity_btn, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(min_price_num, gbc);
        gbc.gridx = 1;
        add(min_price_lbl, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(max_price_num, gbc);
        gbc.gridx = 1;
        add(max_price_lbl, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(price_btn, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(participants_num, gbc);
        gbc.gridx = 1;
        add(participants_lbl, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(participants_btn, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 6;
        add(random_activity_btn, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 7;
        add(add_to_file_btn, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 8;
        add(close_btn, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 9;
        add(activity_lbl, gbc);

        setSize(500,300);
//        setExtendedState(Frame.MAXIMIZED_BOTH);
        setTitle("Bored?");
        price_btn.addActionListener(this);
        participants_btn.addActionListener(this);
        type_activity_btn.addActionListener(this);
        random_activity_btn.addActionListener(this);
        add_to_file_btn.addActionListener(this);
        close_btn.addActionListener(this);
    }

    public void addToFile() throws MalformedURLException, IOException, ParseException {
        File file = new File("C:\\Users\\denis\\Downloads", "Activities.txt");
        file.createNewFile();
        FileWriter writer = new FileWriter(file, true);
        writer.write(activity_lbl.getText());
        if (activity_lbl.getText().length() != 0) {
            writer.append('\n');
        }
        writer.flush();
    }

    public void getActivity(String url) throws IOException, ParseException {
        URL UrlObj = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) UrlObj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        System.out.println("Send 'HTTP GET' request to : " + url);

        int responseCode = connection.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = inputReader.readLine()) != null) {
                response.append(inputLine);
            }

            Object obj = new JSONParser().parse(response.toString());
            // Кастим obj в JSONObject
            JSONObject jo = (JSONObject) obj;
            // Достаём инфу
            String activity = (String) jo.get("activity");
            this.activity_lbl.setText(activity);
            revalidate();
            System.out.println("activity: " + activity);

            inputReader.close();
            System.out.println(response);
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource()==price_btn)
        {
            try {
                getActivity("http://www.boredapi.com/api/activity?minprice=" + min_price_num.getText() + "&maxprice=" + max_price_num.getText());
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }

        if(ae.getSource()==participants_btn)
        {
            try {
                getActivity("http://www.boredapi.com/api/activity?participants=" + participants_num.getText());
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }

        if(ae.getSource()==type_activity_btn)
        {
            try {
                getActivity("http://www.boredapi.com/api/activity?type=" + chooser.getItem(chooser.getSelectedIndex()));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }

        if(ae.getSource()==add_to_file_btn)
        {
            try {
                addToFile();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }

        if(ae.getSource()==random_activity_btn)
        {
            try {
                getActivity("http://www.boredapi.com/api/activity");
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        if(ae.getSource()==close_btn)
        {
            System.exit(0);
        }
    }

    public static void main(String[] args) throws IOException {
        Bored calC = new Bored();
        calC.setVisible(true);
        calC.setLocation(300,300);
    }
}
