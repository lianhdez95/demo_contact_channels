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
              _app_button('Contact Channel', '/contact_channel', context),
              const SizedBox(height: 20),
              _app_button('Contact App Channel', '/contact_picker_channel', context),
              const SizedBox(height: 20),
              _app_button('Battery Level Channel', '/battery_channel', context),
              const SizedBox(height: 20),
              _app_button('Connection Type Channel', '/connection_channel', context),
              const SizedBox(height: 20),
              _app_button('Gallery Channel', '', context),
              const SizedBox(height: 20),
              _app_button('Camera Channel', '', context),
              const SizedBox(height: 20),
              _app_button('Device Info Channel', '', context)
            ],
          ),
        ),
      ),
    );
  }

  Widget _app_button(String text, String route, BuildContext context){
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
          shape: MaterialStateProperty.all<RoundedRectangleBorder>(
              RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(18.0))),
          minimumSize:
          MaterialStateProperty.all<Size>(const Size(300, 50))),
      child: Text(
        text,
        style: const TextStyle(fontSize: 20),
      ),
    );
  }
}

