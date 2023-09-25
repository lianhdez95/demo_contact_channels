import 'package:flutter/material.dart';

class HomePage extends StatelessWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Platform Channels Demo')),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(50),
          child: Column(
            children: [
              _appButton('Contact Channel', '/contact_channel', context, const Icon(Icons.account_box)),
              const SizedBox(height: 20),
              _appButton('Contact Pick Channel', '/contact_picker_channel', context, const Icon(Icons.account_box)),
              const SizedBox(height: 20),
              _appButton('Battery Level Channel', '/battery_channel', context, const Icon(Icons.battery_alert_sharp)),
              const SizedBox(height: 20),
              _appButton('Connection Type Channel', '/connection_channel', context, const Icon(Icons.network_check)),
              const SizedBox(height: 20),
              _appButton('Gallery Channel', '/gallery_pick_channel', context, const Icon(Icons.image)),
              const SizedBox(height: 20),
              _appButton('Camera Channel', '/camera_open_channel', context, const Icon(Icons.camera_alt)),
              const SizedBox(height: 20),
              _appButton('Device Info Channel', '/device_info_channel', context, const Icon(Icons.info)),
              const SizedBox(height: 20),
              _appButton('Location Channel', '/location_channel', context, const Icon(Icons.location_on))
            ],
          ),
        ),
      ),
    );
  }

  Widget _appButton(String text, String route, BuildContext context, Icon icon) {
    return ElevatedButton(
      onPressed: () {
        // Agrega el código para navegar a la ruta correspondiente
        route.isEmpty
            ? ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
                content: Text('No implementado'),
                duration: Duration(seconds: 2),
              ))
            : Navigator.pushNamed(context, route);
      },
      style: ButtonStyle(
          alignment: Alignment.centerLeft,
          shape: MaterialStateProperty.all<RoundedRectangleBorder>(
              RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(18.0))),
          minimumSize: MaterialStateProperty.all<Size>(const Size(300, 50))),
      child: Row(
        children: [
          icon,
          const SizedBox(width: 10,),
          Text(
            text,
            style: const TextStyle(fontSize: 20),
          ),
        ],
      ),
    );
  }
}
