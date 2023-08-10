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
              ElevatedButton(
                onPressed: () {
                  Navigator.pushNamed(context, '/contact_channel');
                },
                style: ButtonStyle(
                    shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                        RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(18.0))),
                    minimumSize:
                        MaterialStateProperty.all<Size>(const Size(300, 50))),
                child: const Text(
                  'Contact Channel',
                  style: TextStyle(fontSize: 20),
                ),
              ),
              const SizedBox(height: 20),
              
              ElevatedButton(
                onPressed: () {
                  Navigator.pushNamed(context, '/contact_picker_channel');
                  // ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
                  //   content: Text('No implementado'),
                  //   duration: Duration(seconds: 2),
                  // ));
                },
                style: ButtonStyle(
                    shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                        RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(18.0))),
                    minimumSize:
                        MaterialStateProperty.all<Size>(const Size(300, 50))),
                child: const Text(
                  'UsingContactApp Channel',
                  style: TextStyle(fontSize: 20),
                ),
              ),
              const SizedBox(height: 20),
              
              ElevatedButton(
                onPressed: () {
                  Navigator.pushNamed(context, '/battery_channel');
                },
                style: ButtonStyle(
                    shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                        RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(18.0))),
                    minimumSize:
                        MaterialStateProperty.all<Size>(const Size(300, 50))),
                child: const Text('Battery Level Channel',
                    style: TextStyle(fontSize: 20)),
              ),
              const SizedBox(height: 20),

              ElevatedButton(
                onPressed: () {
                  Navigator.pushNamed(context, '/connection_channel');
                  // ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
                  //   content: Text('No implementado'),
                  //   duration: Duration(seconds: 2),
                  // ));
                },
                style: ButtonStyle(
                    shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                        RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(18.0))),
                    minimumSize:
                        MaterialStateProperty.all<Size>(const Size(300, 50))),
                child: const Text('Connection Type Channel',
                    style: TextStyle(fontSize: 20)),
              ),
              const SizedBox(height: 20),
              
              ElevatedButton(
                onPressed: () {
                  ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
                    content: Text('No implementado'),
                    duration: Duration(seconds: 2),
                  ));
                },
                style: ButtonStyle(
                    shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                        RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(18.0))),
                    minimumSize:
                        MaterialStateProperty.all<Size>(const Size(300, 50))),
                child: const Text('Gallery Channel',
                    style: TextStyle(fontSize: 20)),
              ),
              const SizedBox(height: 20),
              
              ElevatedButton(
                onPressed: () {
                  // Agrega el código para navegar a la ruta correspondiente
                  ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
                    content: Text('No implementado'),
                    duration: Duration(seconds: 2),
                  ));
                },
                style: ButtonStyle(
                    shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                        RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(18.0))),
                    minimumSize:
                        MaterialStateProperty.all<Size>(const Size(300, 50))),
                child: const Text(
                  'Camera Channel',
                  style: TextStyle(fontSize: 20),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

