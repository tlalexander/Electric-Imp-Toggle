This app uses HTTP POST to send either a "0" or a "1" to any URL you specify via a QR code scan.

Its designed for use with an electric imp, to make turning something on and off from an Android phone very simple. I'm not a "real" app developer, so look at this as a basic sample app to get started. I don't intend to add much functionality beyond what's here, so please fork this and use as desired! Its licensed Apache, which I think means "go hog wild".

Setting up your imp to receive the request is simple. The absolute most basic imp program you could use this with is this:

----------------------------
//Http Test example code
server.log("Started");

class inputHTTP extends InputPort {
    function set(httpVal) {
        server.log("Received val "+httpVal);
    }    
}

// Register with the server
imp.configure("httpTest", [inputHTTP()], []);
// End of code.

----------------------------

The above code will take a value from an HTTP POST and display it in the server log. To use it, create a new imp code file, name it "http test", paste the contents above (whats in between the dashes) into the code file, and save it. Then go to your planner, hit "add node", and select "HTTP in". You may need to click in the planner to see it, but there should be a new node called "HTTP IN". Select your imp in the planner, click settings at the top right, and load the "http test" code. Then click the plus sign at the top right of the HTTP IN node, and connect it to your imp's node.

Now that you have an HTTP IN node, you can get its custom URL, which you will need to give to the Android app. Doing this is easy. Click on the node, then click the settings in the top right corner. That will display a window that contains a URL like this: https://api.electricimp.com/v1/299e35ee19ed1771/305cef2702105b5f

We will create a QR code with that link that we can scan with this app. To do that, go to http://zxing.appspot.com/generator and in the "contents" dropdown at the top, select "URL". Now go back to the planner, copy the URL from the HTTP IN node settings (which, again, will look like this: https://api.electricimp.com/v1/299e35ee19ed1771/305cef2702105b5f), and paste it into the QR code generator under "URL". Then click "generate", and you should have your QR code.

Open the Android app and hit "Scan Code". You may be directed to download the necessary QR code scanning app from the market. If so, download it, then hit the back button or go home and navigate back to this app, and hit "Scan Code" again. Once the scanner comes up, scan the QR code you generated on the above page. If successful, you should be returned to this app with two new buttons: "Turn On" and "Turn Off". Pressing those will send a 1 or a 0 (respectively) to the electric imp connected to our HTTP IN node. Lets see how that works!

The imp code above code posts to the server (it doesn't turn any LEDs on on your imp or anything), so currently, we need to open the code from the planner to see that. Click on your imp node in the planner, and then click the settings in the top right corner. You should see a link to "edit http test". Click that link, and you should now see the code editor with a log window below. (The way the code editor currently works, you won't get a log window unless you navigate to the code that way. That will change in time, however.)

The log window should say "Started", to indicate your imp is running your code. Hit the "Turn On" button on your Android app, and you should see "Received val 1" in the log window. If you do, everything is working! If not, go back and make sure you did everything properly.

If it worked, there is additional code under the file "hannah.imp" that will toggle an LED on the hannah board. Most of the code is just taken from the hannah default sample, but the bits at the end are modified to take an HTTP request.
